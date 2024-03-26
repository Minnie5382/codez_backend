package nob.codez.service;

import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.domain.Problem;
import nob.codez.domain.SolvingRecord;
import nob.codez.domain.User;
import nob.codez.domain.UserSolvingStatus;
import nob.codez.enums.SolvingStatus;
import nob.codez.repository.UserSolvingStatus.UserSolvingStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSolvingStatusService {
    private final UserSolvingStatusRepository ussRepository;
    private final LevelService levelService;

    // 유저가 문제를 제출할 때 UserSolvingStatus 테이블을 업데이트하는 메서드
    @Transactional
    public void updateUserSolvingStatus(SolvingRecord solvingRecord) {
        User user = solvingRecord.getUser();
        Problem problem = solvingRecord.getProblem();

        Optional<UserSolvingStatus> userSolvingStatus = ussRepository.findByUserIdAndProblemId(user.getId(), problem.getId());
        userSolvingStatus.ifPresentOrElse(
                uss -> { // 기록이 있다면
                    if (uss.getSolvingStatus() == SolvingStatus.FAIL && solvingRecord.isCorrect()) { // 이전 기록이 FAIL 기록이고, 유저가 이번에 맞았으면 업데이트
                        ussRepository.save(uss.toBuilder()
                                .solvingStatus(SolvingStatus.SUCCESS)
                                .build());
                        try { levelService.expUp(user.getId(), problem.getExp()); } catch (BaseException e) {throw new RuntimeException(e);}
                    }
        },
                () -> { // 기록이 없다면
                    ussRepository.save(UserSolvingStatus.builder()
                            .user(user)
                            .problem(problem)
                            .solvingStatus(solvingRecord.isCorrect() ? SolvingStatus.SUCCESS : SolvingStatus.FAIL)
                            .build());
                    if (solvingRecord.isCorrect()) {
                        try { levelService.expUp(user.getId(), problem.getExp()); } catch (BaseException e) {throw new RuntimeException(e);}
                    }
                });


    }

}
