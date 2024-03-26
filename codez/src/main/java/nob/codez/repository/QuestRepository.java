package nob.codez.repository;

import nob.codez.domain.Quest;
import nob.codez.domain.QuestConditionType;
import nob.codez.enums.QuestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface QuestRepository extends JpaRepository<Quest, Long>{
    @Query("SELECT q FROM Quest q WHERE q.questConditionType <> 'ATTENDANCE' AND q.questType=:questType AND q.status = 'ACTIVE'")

    List<Quest> findAllByQuestTypeButAttendQuest(QuestType questType);

    @Query("SELECT q FROM Quest q WHERE q.questConditionType = 'ATTENDANCE' AND q.status = 'ACTIVE'")
    Optional<Quest> findAttendanceQuest();

}