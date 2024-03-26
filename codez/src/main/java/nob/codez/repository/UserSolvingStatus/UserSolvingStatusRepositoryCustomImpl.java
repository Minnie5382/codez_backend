package nob.codez.repository.UserSolvingStatus;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nob.codez.domain.UserSolvingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static nob.codez.domain.QUserSolvingStatus.userSolvingStatus;

@RequiredArgsConstructor
public class UserSolvingStatusRepositoryCustomImpl implements UserSolvingStatusRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public List<UserSolvingStatus> findAllByUserIdByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 종료일을 해당 날짜의 마지막 시간으로 설정

        return query
                .selectFrom(userSolvingStatus)
                .where(userSolvingStatus.updatedAt.between(startDateTime, endDateTime))
                .fetch();


    }
}
