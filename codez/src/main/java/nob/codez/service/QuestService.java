package nob.codez.service;

import lombok.RequiredArgsConstructor;
import nob.codez.domain.Quest;
import nob.codez.domain.QuestConditionType;
import nob.codez.enums.QuestType;
import nob.codez.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final QuestRepository questRepository;
//    public void insertQuests() {
//        // 일일 퀘스트 생성
//        List<Quest> dailyQuests = new ArrayList<>();
//        dailyQuests.add(createDailyQuest(QuestConditionType.ATTENDANCE));
//        dailyQuests.add(createDailyQuest(QuestConditionType.HASH, 1));
//        dailyQuests.add(createDailyQuest(QuestConditionType.STACKQUEUE, 1));
//        dailyQuests.add(createDailyQuest(QuestConditionType.HEAP, 1));
//        dailyQuests.add(createDailyQuest(QuestConditionType.SORT, 1));
//        dailyQuests.add(createDailyQuest(QuestConditionType.DFSBFS, 1));
//        dailyQuests.add(createDailyQuest(QuestConditionType.GREEDY, 1));
//        dailyQuests.add(createDailyQuest(QuestConditionType.DP, 1));
//
//        // 주간 퀘스트 생성
//        List<Quest> weeklyQuests = new ArrayList<>();
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.HASH, 3));
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.STACKQUEUE, 3));
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.HEAP, 3));
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.SORT, 3));
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.DFSBFS, 3));
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.GREEDY, 3));
//        weeklyQuests.add(createWeeklyQuest(QuestConditionType.DP, 3));
//
//        // 저장
//        dailyQuests.forEach(q -> questRepository.save(q));
//        weeklyQuests.forEach(q -> questRepository.save(q));
//
//    }
//
//    private Quest createDailyQuest(QuestConditionType conditionType, int questConditionNumber) {
//        return Quest.builder()
//                .questType(QuestType.DAILY)
//                .name(conditionType.toString())
//                .description(conditionType == QuestConditionType.ATTENDANCE ? "" : questConditionNumber + "문제 풀기")
//                .exp(conditionType == QuestConditionType.ATTENDANCE ? 5 : 10)
//                .questConditionType(conditionType)
//                .questConditionNumber(questConditionNumber)
//                .build();
//    }
//
//    private Quest createDailyQuest(QuestConditionType conditionType) {
//        return Quest.builder()
//                .questType(QuestType.DAILY)
//                .name(conditionType.toString())
//                .description("")
//                .exp(10)
//                .questConditionType(conditionType)
//                .questConditionNumber(0)
//                .build();
//    }
//
//    private Quest createWeeklyQuest(QuestConditionType conditionType, int questConditionNumber) {
//        return Quest.builder()
//                .questType(QuestType.WEEKLY)
//                .name(conditionType.toString())
//                .description(questConditionNumber + "문제 풀기")
//                .exp(30)
//                .questConditionType(conditionType)
//                .questConditionNumber(questConditionNumber)
//                .build();
//    }

}
