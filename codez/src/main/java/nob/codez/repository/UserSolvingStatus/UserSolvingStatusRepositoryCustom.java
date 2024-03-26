package nob.codez.repository.UserSolvingStatus;

import nob.codez.domain.UserSolvingStatus;

import java.time.LocalDate;
import java.util.List;

public interface UserSolvingStatusRepositoryCustom {
    public List<UserSolvingStatus> findAllByUserIdByPeriod(Long userId, LocalDate startDate, LocalDate endDate);
}
