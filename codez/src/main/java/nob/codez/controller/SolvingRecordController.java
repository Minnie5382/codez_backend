package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nob.codez.domain.Problem;
import nob.codez.domain.SolvingRecord;
import nob.codez.domain.User;
import nob.codez.dto.postRecordReq;
import nob.codez.repository.Problem.ProblemRepository;
import nob.codez.repository.SolvingRecordRepository;
import nob.codez.repository.UserRepository;
import nob.codez.service.UserSolvingStatusService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Solving Record")
public class SolvingRecordController {
    private final SolvingRecordRepository solvingRecordRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final UserSolvingStatusService ussService;
    /**
     * (테스트용) 소스코드 제출 및 채점 임시 API
     */
    @PostMapping("/admin/problems/solutions")
    @Operation(summary = "(임시) 소스코드 제출 및 채점", description = "임시 API, 디벨롭 예정")
    public String submitSourceCode(@RequestBody postRecordReq postRecordReq) {
        Optional<Problem> problem = problemRepository.findById(postRecordReq.getProblemId());
        Optional<User> user = userRepository.findById(postRecordReq.getUserId());
        boolean isCorrect = postRecordReq.isCorrect();

        // SolvingRecord 에 문제풀이 기록 저장
        SolvingRecord savedRecord = solvingRecordRepository.save(SolvingRecord.builder()
                .user(user.get())
                .problem(problem.get())
                .isCorrect(isCorrect)
                .build());

        // UserSolvingRecord 에 최종 풀이 상태 저장
        ussService.updateUserSolvingStatus(savedRecord);

        return "문제풀이 기록 저장 완료!";
    }
}
