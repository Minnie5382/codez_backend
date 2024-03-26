package nob.codez.repository;

import nob.codez.domain.Quest;
import nob.codez.domain.QuestConditionType;
import nob.codez.domain.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserQuestRepository extends JpaRepository<UserQuest, Long> {
    //해당 유저의 모든 퀘스트 목록 조회
    @Query("SELECT uq FROM UserQuest uq WHERE uq.user.id = :userId AND uq.status = 'ACTIVE'")
    List<UserQuest> findAllByUserId(Long userId);

    //완료된 유저퀘스트 목록 조회
    @Query("SELECT uq FROM UserQuest uq WHERE uq.user.id = :userId AND uq.isDone = true AND uq.status = 'ACTIVE'")
    List<UserQuest> findDoneByUserId(Long userId);

    //완료처리하고 싶은 퀘스트 조회
    @Query("SELECT uq FROM UserQuest uq WHERE uq.user.id = :userId AND uq.quest.id IN :questIds AND uq.status = 'ACTIVE'")
    List<UserQuest> findClearQuests(Long userId, List<Long> questIds);

    //데일리 퀘스트만 조회
    @Query("SELECT uq FROM UserQuest uq WHERE uq.user.id = :userId AND uq.quest.questType = 'DAILY' AND uq.status = 'ACTIVE'")
    List<UserQuest> findDailyByUserId(Long userId);
    //위클리 퀘스트만 조회
    @Query("SELECT uq FROM UserQuest uq WHERE uq.user.id = :userId AND uq.quest.questType = 'WEEKLY' AND uq.status = 'ACTIVE'")
    List<UserQuest> findWeeklyByUserId(Long userId);

    //알고리즘 분류에 따라 해당 유저의 유저퀘스트 조회
    @Query("SELECT uq FROM UserQuest uq JOIN uq.quest q WHERE uq.user.id = :userId AND q.questConditionType = :questConditionType AND uq.status = 'ACTIVE'")
    List<UserQuest> findByQuestConditionType(Long userId, QuestConditionType questConditionType);

    //한 유저의 모든 유저퀘스트 삭제
    @Query("UPDATE UserQuest uq SET uq.status = 'DELETED' WHERE uq.user.id = :userId")
    List<UserQuest> deleteAllByUserId(Long userId);


}
