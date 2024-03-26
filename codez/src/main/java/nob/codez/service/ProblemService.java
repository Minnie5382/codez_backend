package nob.codez.service;

import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.domain.*;
import nob.codez.dto.ExecutedResultDTO;
import nob.codez.domain.*;
import nob.codez.dto.GetProblemResponse;
import nob.codez.dto.TestcaseDto;
import nob.codez.enums.Difficulty;
import nob.codez.enums.ProblemType;
import nob.codez.enums.ResultDetail;
import nob.codez.repository.Problem.ProblemCorrectStatRepository;
import nob.codez.repository.Problem.ProblemRepository;
import nob.codez.repository.SolvingRecordRepository;
import nob.codez.repository.TestCaseRepository;
import nob.codez.repository.UserRepository;
import nob.codez.service.LevelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static nob.codez.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemCorrectStatRepository pcsRepository;
    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;
    private final SolvingRecordRepository solvingRecordRepository;
    private final LevelService levelService;

    public static void closeConnection(Socket socket, BufferedReader in, PrintWriter out, String terminal) throws IOException {
        if (terminal != null && !terminal.equals(""))
            out.println(terminal);
        out.close();
        in.close();
        socket.close();
    }

    public boolean compileCode(String code) throws BaseException {
        try {
            Socket socket = new Socket("exec", 4000); //(도커 컨테이너 이름, 포트번호)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("COMPILE");
            out.println(code);
            out.println("EOF");
            BufferedReader inResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = inResponse.readLine();
            closeConnection(socket, inResponse, out, null);
            if (result.equals("COMPILE_SUCCESS")) return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(SOKET_CONNECT_FAIL);
        }
        return false;
    }

    public void addSolvingRecord(Long userId, Long problemId, ExecutedResultDTO[] result) throws BaseException {
        Problem problem = problemRepository.findById(problemId).get();
        User user = userRepository.findById(userId).get();
        ResultDetail rd;
        if (result == null)
            rd = ResultDetail.COMPILE;
        else {
            rd = ResultDetail.SUCCESS;
            for (ExecutedResultDTO r : result) {
                String str = r.getResult();
                if (!str.equals("CORRECT")) {
                    if (str.equals("RUNTIME_ERROR"))
                        rd = ResultDetail.RUNTIME;
                    else if (str.equals("TIMED_OUT"))
                        rd = ResultDetail.TIMEOUT;
                    else
                        rd = ResultDetail.INCORRECT;
                    break;
                }
            }
        }

        SolvingRecord record = SolvingRecord.builder()
                .isCorrect(rd == ResultDetail.SUCCESS)
                .resultDetail(rd)
                .executionTime(problem.getTimeLimit())
                .problem(problem)
                .user(user)
                .build();
        solvingRecordRepository.save(record);

        //여기에 처음 문제를 풀었을때만 경험치 얻도록
        if(solvingRecordRepository.findFirstCorrectRecords(userId, problemId).size() == 1){
            levelService.expUp(userId, problem.getExp());
        }

    }

    public ExecutedResultDTO[] executeCode(Long problemId, int timeLimit, int length) throws BaseException {
        List<TestCase> byProblemId = testCaseRepository.findByProblemId(problemId);//짠 테스트케이스! -> 개쌉쎈스쟁의

        List<ExecutedResultDTO> ret = new ArrayList<>();
        try {
            Socket socket = new Socket("exec", 4000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            length = byProblemId.size() < length ? byProblemId.size() : length;
            out.println("EXECUTE");
            for (int i = 0; i < length; i++)
                out.println(byProblemId.get(i).getInput());
            out.println("EOF");
            BufferedReader inResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            for (int i = 0; i < length; i++) {
                String result = inResponse.readLine();
                String output = "";
                Long elapsedTime = 0L;
                for (String s = null; (s = inResponse.readLine()) != null && !s.equals("DONE") && !s.equals("EOF");)
                    output += s;
                if (result.equals("STDOUT")) {
                    elapsedTime = Long.parseLong(inResponse.readLine());
                    inResponse.readLine(); // EOF
                }
                ret.add(new ExecutedResultDTO(i + 1, byProblemId.get(i).getInput(), output, result, elapsedTime));
            }
            closeConnection(socket, inResponse, out, null);
        }

        catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(SOKET_CONNECT_FAIL);
        }
        return gradeCode(byProblemId, ret, timeLimit);
    }

    public ExecutedResultDTO[] gradeCode(List<TestCase> testcase, List<ExecutedResultDTO> ret, long timeLimit) {
        for (int i = 0; i < ret.size(); i++) {
            if (ret.get(i).getResult().equals("STDOUT")) {
                if (timeLimit * 1e6 < ret.get(i).getElapsedTime())
                    ret.get(i).setResult("TIMEOUT");
                else {
                    String trimmedOutput = testcase.get(i).getOutput().trim().replaceAll("^\\[|\\]$", "").trim();
                    if (ret.get(i).getOutput().equals(trimmedOutput))
                        ret.get(i).setResult("CORRECT");
                    else ret.get(i).setResult("INCORRECT");
                }
            }
        }
        return ret.toArray(new ExecutedResultDTO[0]);
    }

    public Long addProblem (String title,
                           String content,
                           Difficulty difficulty,
                           int timeLimit,
                           int exp,
                           ProblemType problemType,
                           String inputFormatJava,
                           List<TestcaseDto> testCaseDtoList) {

        ProblemCorrectStat pcs = ProblemCorrectStat.builder().build();

        Problem problem = Problem.builder()
                .title(title)
                .content(content)
                .difficulty(difficulty)
                .timeLimit(timeLimit)
                .exp(exp)
                .problemType(problemType)
                .inputFormatJava(inputFormatJava)
                .problemCorrectStat(pcs)
                .build();

        pcsRepository.save(pcs);
        Problem savedProblem = problemRepository.save(problem);

        List<TestCase> testCaseList = new ArrayList<>();
        testCaseDtoList.forEach(tc -> {
            TestCase testcase = TestCase.builder()
                    .index(tc.getCaseNumber())
                    .input(tc.getInput())
                    .output(tc.getOutput())
                    .problem(problem)
                    .build();
            testCaseList.add(testcase);
            testCaseRepository.save(testcase);
        });
        return savedProblem.getId();
    }

    public GetProblemResponse getProblem(Long problemId) throws BaseException {
        GetProblemResponse response = problemRepository.findById(problemId)
                .map(p -> GetProblemResponse.builder()
                        .problemId(p.getId())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .inputFormat(p.getInputFormatJava())
                        .timeLimit(p.getTimeLimit())
                        .build())
                .orElseThrow(() -> new BaseException(INVALID_PROBLEM));

        return response;
    }


}
