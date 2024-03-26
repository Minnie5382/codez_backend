package nob.codez.controller;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.config.response.BaseResponse;
import static nob.codez.config.response.BaseResponseStatus.CODE_COMPILE_FAIL;
import nob.codez.domain.Problem;
import nob.codez.dto.*;
import nob.codez.domain.UserSolvingStatus;
import nob.codez.dto.GetProblemResponse;
import nob.codez.dto.GetProblemsResponse;
import nob.codez.dto.PostProblemRequest;
import nob.codez.dto.ProblemInfo;
import nob.codez.enums.SolvingStatus;
import nob.codez.repository.Problem.ProblemRepository;
import nob.codez.repository.UserSolvingStatus.UserSolvingStatusRepository;
import nob.codez.service.ProblemService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nob.codez.config.response.BaseResponseStatus.NOT_LOGGED_IN_USER;
import static nob.codez.config.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@Tag(name = "Problem", description = "문제 생성, 풀이 등 문제과 관련된 API")
public class ProblemController {
    private final ProblemService problemService;
    private final ProblemRepository problemRepository;
    private final UserSolvingStatusRepository ussRepository;

    /**
     * 문제 목록 조회 API
     * @param session
     * @return
     */
    @GetMapping("/api/problems")
    @Operation(summary = "문제 목록 조회")
    public BaseResponse<GetProblemsResponse> getProblemList(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        List<ProblemInfo> problemList = problemRepository.findAll().stream().map(p -> {
            Long problemId = p.getId();
            Optional<UserSolvingStatus> uss = ussRepository.findByProblemId(problemId);
            return ProblemInfo.builder()
                    .problemId(problemId)
                    .title(p.getTitle())
                    .solvingStatus(uss.isPresent() ? uss.get().getSolvingStatus() : SolvingStatus.NONE)
                    .correctRate(p.getProblemCorrectStat().getCorrectRate())
                    .difficulty(p.getDifficulty())
                    .submitNum(p.getProblemCorrectStat().getSubmitNum())
                    .type(p.getProblemType())
                    .build();
        }).collect(Collectors.toList());

        int totalPageNum = problemList.size();

        GetProblemsResponse response = GetProblemsResponse.builder().problemList(problemList).totalPageNum(totalPageNum).build();
        return new BaseResponse<>(response);

    }

    /**
     * 문제 단건 조회 API
     * @param problemId
     * @param session
     * @return
     */
    @GetMapping("/api/problems/{problemId}")
    @Operation(summary = "문제 단건 조회")
    @Parameters({@Parameter(name = "problemId", description = "문제 id", example = "1")})
    public BaseResponse<GetProblemResponse> getProblem(@PathVariable("problemId") Long problemId,
                                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        try {
            return new BaseResponse<>(problemService.getProblem(problemId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //문제 등록
    @PostMapping("/admin/problems/register")
    public BaseResponse<PostProblemResponse> Register(@RequestBody PostProblemRequest request) {
        try {
            boolean isCompileSuccess = problemService.compileCode(request.getInputFormatJava());
            if(isCompileSuccess) {
                problemService.addProblem(
                    request.getTitle(),
                    request.getContent(),
                    request.getDifficulty(),
                    request.getTimeLimit(),
                    request.getExp(),
                    request.getProblemType(),
                    request.getInputFormatJava(),
                    request.getTestcaseDtoList()
                );
                return new BaseResponse<>(SUCCESS);
            }
            return new BaseResponse<>(CODE_COMPILE_FAIL);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/api/problems/execute")
    public BaseResponse<SolutionResultResponse> execute(@RequestBody ExecuteProblemRequest req) {
        Long problemId = req.getProblemId();
        String code = req.getCode();
        int timeLimit = Integer.parseInt(req.getTimeLimit());
        int testcaseNum = 3;
        try {
            boolean isCompileSuccess = problemService.compileCode(code);
            if (!isCompileSuccess)
                return new BaseResponse<>(CODE_COMPILE_FAIL);
            ExecutedResultDTO[] result = new ExecutedResultDTO[testcaseNum];
            result = problemService.executeCode(problemId, timeLimit, testcaseNum);
            SolutionResultResponse response = new SolutionResultResponse(result);
            return new BaseResponse<>(response);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/api/problems/submit")
    public BaseResponse<SolutionResultResponse> submit(HttpSession session, @RequestBody ExecuteProblemRequest req) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);
        
        Long problemId = req.getProblemId();
        String code = req.getCode();
        int timeLimit = Integer.parseInt(req.getTimeLimit());
        int testcaseNum = 20;

        SolutionResultResponse response;
        ExecutedResultDTO[] result = null;
        try {
            boolean isCompileSuccess = problemService.compileCode(code);
            if (isCompileSuccess) {
                result = problemService.executeCode(problemId, timeLimit, testcaseNum);
                response = new SolutionResultResponse(result);
                problemService.addSolvingRecord(
                    userId,
                    problemId,
                    result
                );
            }
            else
                response = new SolutionResultResponse(new ExecutedResultDTO[0]);
            return new BaseResponse<>(response);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}