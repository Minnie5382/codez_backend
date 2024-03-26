package nob.codez.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nob.codez.domain.Level;
import nob.codez.domain.Quest;
import nob.codez.domain.QuestConditionType;
import nob.codez.dto.TestcaseDto;
import nob.codez.enums.Difficulty;
import nob.codez.enums.ProblemType;
import nob.codez.enums.QuestType;
import nob.codez.enums.Rank;
import nob.codez.repository.LevelRepository;
import nob.codez.repository.QuestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 스프링 애플리케이션 실행 직후 실행되는 로직
@Component
@RequiredArgsConstructor
public class InitService {
    private final LevelRepository levelRepository;
    private final QuestRepository questRepository;
    private final ProblemService problemService;

    @PostConstruct
    public void init() {
        createDefaultLevels(); // 레벨 데이터
        createDefaultQuests(); // 퀘스트 데이터
        createDummyProblem(); // 문제 데이터
    }

    // 레벨 데이터 삽입하는 메서드
    public void createDefaultLevels() {
        for (int i = 0; i <= 50; i++) {
            int maxExp = (int) Math.round(100 * Math.pow(1.1, i));
            if (i == 0) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.INTERN)
                        .expMax(20)
                        .build());
            }
            if (1 <= i && i < 10) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.INTERN)
                        .expMax(maxExp)
                        .build());
            }
            if (10 <= i && i < 20) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.JUNIOR)
                        .expMax(maxExp)
                        .build());
            }
            if (20 <= i && i < 30) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.SENIOR)
                        .expMax(maxExp)
                        .build());
            }
            if (30 <= i && i < 40) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.EXPERT)
                        .expMax(maxExp)
                        .build());
            }
            if (40 <= i && i < 50) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.MASTER)
                        .expMax(maxExp)
                        .build());
            }
            if (i == 50) {
                levelRepository.save(Level.builder()
                        .level((long) i)
                        .rank(Rank.GOD)
                        .expMax(Integer.MAX_VALUE)
                        .build());
            }
        }
    }

    // 퀘스트 데이터 삽입하는 메서드
    public void createDefaultQuests() {
        // 일일 퀘스트 생성
        List<Quest> dailyQuests = new ArrayList<>();
        dailyQuests.add(createDailyQuest(QuestConditionType.ATTENDANCE));
        dailyQuests.add(createDailyQuest(QuestConditionType.HASH, 1));
        dailyQuests.add(createDailyQuest(QuestConditionType.STACKQUEUE, 1));
        dailyQuests.add(createDailyQuest(QuestConditionType.HEAP, 1));
        dailyQuests.add(createDailyQuest(QuestConditionType.SORT, 1));
        dailyQuests.add(createDailyQuest(QuestConditionType.DFSBFS, 1));
        dailyQuests.add(createDailyQuest(QuestConditionType.GREEDY, 1));
        dailyQuests.add(createDailyQuest(QuestConditionType.DP, 1));

        // 주간 퀘스트 생성
        List<Quest> weeklyQuests = new ArrayList<>();
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.HASH, 3));
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.STACKQUEUE, 3));
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.HEAP, 3));
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.SORT, 3));
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.DFSBFS, 3));
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.GREEDY, 3));
        weeklyQuests.add(createWeeklyQuest(QuestConditionType.DP, 3));

        // 저장
        dailyQuests.forEach(q -> questRepository.save(q));
        weeklyQuests.forEach(q -> questRepository.save(q));

    }

    private Quest createDailyQuest(QuestConditionType conditionType, int questConditionNumber) {
        return Quest.builder()
                .questType(QuestType.DAILY)
                .name(mapQuestName(conditionType))
                .description(conditionType == QuestConditionType.ATTENDANCE ? "" : questConditionNumber + "문제 풀기")
                .exp(conditionType == QuestConditionType.ATTENDANCE ? 5 : 10)
                .questConditionType(conditionType)
                .questConditionNumber(questConditionNumber)
                .build();
    }

    private Quest createDailyQuest(QuestConditionType conditionType) {
        return Quest.builder()
                .questType(QuestType.DAILY)
                .name(mapQuestName(conditionType))
                .description("")
                .exp(10)
                .questConditionType(conditionType)
                .questConditionNumber(0)
                .build();
    }

    private Quest createWeeklyQuest(QuestConditionType conditionType, int questConditionNumber) {
        return Quest.builder()
                .questType(QuestType.WEEKLY)
                .name(mapQuestName(conditionType))
                .description(questConditionNumber + "문제 풀기")
                .exp(30)
                .questConditionType(conditionType)
                .questConditionNumber(questConditionNumber)
                .build();
    }

    private String mapQuestName(QuestConditionType conditionType) {
        String questName = "";
        switch(conditionType) {
            case conditionType.DFSBFS:
                questName = "DFS/BFS";
                break;
            case conditionType.STACKQUEUE:
                questName = "스택/큐";
                break;
            case conditionType.GREEDY:
                questName = "그리디";
                break;
            case conditionType.HASH:
                questName = "";
                break;
            case conditionType.SORT:
                questName = "정렬";
                break;
            case conditionType.DP:
                questName = "동적 프로그래밍";
                break;
            case conditionType.HEAP:
                questName = "힙";
                break;
            default:
                questName = "출석체크";
        }
        return questName;
    }

    // 문제 더미데이터 삽입
    public void createDummyProblem() {
        String titleGreedy = "두 정수 더하기(Greedy)";
        String titleDP = "두 정수 더하기(DP)";
        String titleHEAP = "두 정수 더하기(Heap)";
        String content = "두 정수를 입력받아 더한 값을 출력하세요.";
        Difficulty difficulty = Difficulty.EASY;
        int timeLimit = 1000;
        int exp = 20;
        String inputFormatJava = "public class Solution {\n" +
                "    public int solution(int[] args) {\n" +
                "        int answer = 0;\n" +
                "\n" +
                "        return answer;\n" +
                "    }\n" +
                "\n" +
                "    // \"2 3\"\n" +
                "    public int[] parseInput(String[] args) {\n" +
                "        int[] result = new int[args.length];\n" +
                "        for (int i=0 ; i<args.length ; i++)\n" +
                "            result[i] = Integer.parseInt(args[i]);\n" +
                "        return result;\n" +
                "    }\n" +
                "}";

        TestcaseDto tc1 = TestcaseDto.builder()
                .caseNumber(1)
                .input("[2, 3]")
                .output("5")
                .build();
        TestcaseDto tc2 = TestcaseDto.builder()
                .caseNumber(1)
                .input("[35, 103]")
                .output("138")
                .build();
        TestcaseDto tc3 = TestcaseDto.builder()
                .caseNumber(1)
                .input("[-2, -3]")
                .output("-5")
                .build();
        TestcaseDto tc4 = TestcaseDto.builder()
                .caseNumber(1)
                .input("[0, 0]")
                .output("0")
                .build();
        TestcaseDto tc5 = TestcaseDto.builder()
                .caseNumber(1)
                .input("[-5, 5]")
                .output("0")
                .build();

        List<TestcaseDto> testCaseDtoList = Arrays.asList(tc1, tc2, tc3, tc4, tc5);

        // Greedy
        problemService.addProblem(titleGreedy, content, difficulty, timeLimit, exp, ProblemType.GREEDY, inputFormatJava, testCaseDtoList);

        // DP
        problemService.addProblem(titleDP, content, difficulty, timeLimit, exp, ProblemType.DP, inputFormatJava, testCaseDtoList);

        // Heap
        problemService.addProblem(titleHEAP, content, difficulty, timeLimit, exp, ProblemType.HEAP, inputFormatJava, testCaseDtoList);


    }


}
