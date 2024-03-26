package nob.codez.repository;

import java.util.List;
import nob.codez.domain.SolvingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SolvingRecordRepository extends JpaRepository<SolvingRecord, Long> {
    @Query("SELECT sr FROM SolvingRecord sr WHERE sr.user.id = :userId AND sr.problem.id = :problemId AND sr.isCorrect = TRUE")
    List<SolvingRecord> findFirstCorrectRecords(Long userId, Long problemId);
}
