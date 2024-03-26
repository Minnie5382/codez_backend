package nob.codez.repository.UserSolvingStatus;

import nob.codez.domain.UserSolvingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserSolvingStatusRepository extends JpaRepository<UserSolvingStatus, Long>, UserSolvingStatusRepositoryCustom {
    Optional<UserSolvingStatus> findByUserIdAndProblemId(Long userId, Long ProblemId);

    Optional<UserSolvingStatus> findByProblemId(Long problemId);

    @Query("SELECT COUNT(uss) FROM UserSolvingStatus uss WHERE uss.user.id = :userId AND uss.solvingStatus = 'SUCCESS'")
    int successSubmitNum(Long userId);

    @Query("SELECT COUNT(uss) FROM UserSolvingStatus uss WHERE uss.user.id = :userId AND (uss.solvingStatus = 'SUCCESS' OR uss.solvingStatus = 'FAIL')")
    int totalSubmitNum(Long userId);
}
