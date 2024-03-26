package nob.codez.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.domain.*;
import nob.codez.dto.QuestListDTO;
import nob.codez.enums.SolvingStatus;
import nob.codez.enums.Status;
import nob.codez.repository.QuestRepository;
import nob.codez.repository.UserQuestRepository;
import nob.codez.repository.UserRepository;
import nob.codez.repository.UserSolvingStatus.UserSolvingStatusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static nob.codez.config.response.BaseResponseStatus.QUESTS_IS_EMPTY;
import static nob.codez.domain.QuestConditionType.ATTENDANCE;
import static nob.codez.enums.QuestType.DAILY;
import static nob.codez.enums.QuestType.WEEKLY;

@Service
@RequiredArgsConstructor
@Transactional
public class UserQuestService {
    private final UserQuestRepository userQuestRepository;
    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final LevelService levelService;
    private final UserSolvingStatusRepository ussRepository;


    // 해당 유저의 모든 퀘스트 조회하기
    public List<QuestListDTO> findAllQuests(Long userId) throws BaseException{
        List<UserQuest> userQuests = userQuestRepository.findAllByUserId(userId);
        // 문제 풀이 퀘스트 조건 검사
        isUserQuestClear(userQuests);

        List<QuestListDTO> result = userQuests.stream()
                .map(userQ -> new QuestListDTO(userQ.getQuest(), userQ.isDone(), userQ.isClear()))
                .collect(Collectors.toList());
        if(result.size() == 0) throw new BaseException(QUESTS_IS_EMPTY);

        //출석 퀘스트 clear를 true로.
        for (int i = 0; i < userQuests.size(); i++) {
            if(userQuests.get(i).getQuest().getQuestConditionType() == ATTENDANCE) {
                UserQuest toTrue = userQuests.get(i).toBuilder().isClear(true).build();
                userQuestRepository.save(toTrue);
            }
        }

        // 문제 풀이 퀘스트 조건 검사
        isUserQuestClear(userQuests);

        return result;
    }

    //isClear=true인 퀘스트를 isDone=true로
    public List<Long> getQuestsDone(Long userId, List<Long> questIds) throws BaseException {
        ArrayList<Long> doneQuestIdList = new ArrayList<>();
        ArrayList<UserQuest> userQuestList = new ArrayList<>();
        userQuestRepository.findClearQuests(userId, questIds)
                .forEach(quest -> {
                    if (quest.isClear() && !quest.isDone()) {
                        quest = quest.toBuilder().isDone(true).build();
                        UserQuest savedUserQuest = userQuestRepository.save(quest);
                        userQuestList.add(savedUserQuest);
                        doneQuestIdList.add(savedUserQuest.getQuest().getId());
                    }
                });
        userQuestList.forEach(uq -> {
            try {
                getExpFromQuest(uq);
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        });

        return doneQuestIdList;
    }

    //해당 유저의 isDone=true 퀘스트 조회하기
    public List<QuestListDTO> findDoneQuests(Long userId) throws BaseException {
        List<UserQuest> userQuests = userQuestRepository.findDoneByUserId(userId);

        return userQuests.stream()
                .map(userQ -> new QuestListDTO(userQ.getQuest(), userQ.isDone(), userQ.isClear()))
                .collect(Collectors.toList());

    }

    //일일퀘스트 매일 자정마다 유저에게 자동 재할당
    @Scheduled(cron = "0 0 0 * * ?") //매일 자정 자동으로 메서드 사용
//    @Scheduled(cron = "0 * * * * ?") //잘되나 테스트
    public void resetDailyQuests(){
        // 데이터 찾기
        List<User> allUsers = userRepository.findAll();
        List<Quest> dailyQuests = questRepository.findAllByQuestTypeButAttendQuest(DAILY);
        int dailyQuestsNum = 3; //총 데일리퀘스트 개수

        // 퀘스트 할당
        for (int i=0 ; i<allUsers.size() ; i++) {
            List<UserQuest> userQuests = userQuestRepository.findDailyByUserId(allUsers.get(i).getId());
            for (int j = 0; j < dailyQuestsNum; j++) {
                //원래 있던 퀘스트 소프트딜리트
                UserQuest uq = userQuests.get(j);
                uq = uq.toBuilder()
                        .status(Status.DELETED)
                        .build();
                userQuestRepository.save(uq);

                //새 퀘스트 생성 및 할당
                Quest quest;
                if (j == 0) {
                    quest = questRepository.findAttendanceQuest().get();// 출석 퀘스트
                } else {
                    int randomIdx = (int) ((Math.random() * (dailyQuests.size() - 1)) + 1);
                    quest = dailyQuests.get(randomIdx);
                    dailyQuests.remove(randomIdx);
                }

                UserQuest userQuest = UserQuest.builder()
                        .quest(quest)
                        .user(allUsers.get(i))
                        .build();
                userQuestRepository.save(userQuest);
            }
        }
    }

    // 주간퀘스트 일요일 자정마다 유저에게 자동 재할당
    @Scheduled(cron = "0 0 0 * * SUN")
//    @Scheduled(cron = "0 * * * * ?") //잘되나 테스트
    public void resetWeeklyQuests(){
        // 데이터 찾기
        List<User> allUsers = userRepository.findAll();
        List<Quest> dailyQuests = questRepository.findAllByQuestTypeButAttendQuest(WEEKLY);
        int weeklyQuestsNum = 3; //총 위클리퀘스트 개수

        // 퀘스트 할당
        for (int i=0 ; i<allUsers.size() ; i++) {
            List<UserQuest> userQuests = userQuestRepository.findWeeklyByUserId(allUsers.get(i).getId());
            for (int j = 0; j < weeklyQuestsNum; j++) {
                //원래 있던 퀘스트 소프트딜리트
                UserQuest uq = userQuests.get(j);
                uq = uq.toBuilder()
                        .status(Status.DELETED)
                        .build();
                userQuestRepository.save(uq);

                //새 퀘스트 생성 및 할당
                Quest quest;
                int randomIdx = (int) ((Math.random() * (dailyQuests.size() - 1)) + 1);
                quest = dailyQuests.get(randomIdx);
                dailyQuests.remove(randomIdx);

                UserQuest userQuest = UserQuest.builder()
                        .quest(quest)
                        .user(allUsers.get(i))
                        .endDate(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)))
                        .build();
                userQuestRepository.save(userQuest);
            }
        }
    }

    // 일일퀘스트(출석 퀘스트+랜덤 일일퀘스트 2개), 주간 퀘스트 3개 랜덤으로 유저에게 할당하기
    public void insertRandomQuestToUser(Long userId) {
        // 유저 찾기
        Optional<User> user = userRepository.findById(userId);

        // 일일 퀘스트 할당
        List<Quest> dailyQuestList = questRepository.findAllByQuestTypeButAttendQuest(DAILY);
        for (int i=0 ; i<3 ; i++) {
            Quest quest;
            if (i == 0) {
                quest = questRepository.findAttendanceQuest().get();// 출석 퀘스트
            } else {
//                long randomIdx = new Random().nextInt(dailyQuestList.size()) + 1;

                int randomIdx = (int) ((Math.random() * (dailyQuestList.size() - 1)) + 1);
                quest = dailyQuestList.get(randomIdx);
                dailyQuestList.remove(randomIdx);
            }

            UserQuest userQuest = UserQuest.builder()
                    .quest(quest)
                    .user(user.get())
                    .isClear(false)
                    .build();
            userQuestRepository.save(userQuest);
        }

        // 주간 퀘스트 할당
        List<Quest> weeklyQuestList = questRepository.findAllByQuestTypeButAttendQuest(WEEKLY);
        for (int i=0 ; i<3 ; i++) {
            int randomIdx = (int) ((Math.random() * (weeklyQuestList.size()-1)) + 1);
            Quest quest = weeklyQuestList.get(randomIdx);
            weeklyQuestList.remove(randomIdx);

            UserQuest userQuest = UserQuest.builder()
                    .quest(quest)
                    .user(user.get())
                    .isClear(false)
                    .endDate(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))) // 이번 주의 마지막 날(일요일)
                    .build();
            userQuestRepository.save(userQuest);
        }
    }

    // 퀘스트 달성 시 경험치 획득 메서드
    public void getExpFromQuest(UserQuest userQuest) throws BaseException{
        User user = userQuest.getUser();
        int questExp = userQuest.getQuest().getExp();

        levelService.expUp(user.getId(), questExp);
    }

    // 조건 달성됐는지? 검사하고 충족한 퀘스트는 isClear = true 처리하는 메서드 (출석체크 퀘스트는 제외)
    public void isUserQuestClear(List<UserQuest> userQuestList) {
        for (UserQuest userQuest : userQuestList) { // 하나씩 돌면서
            Quest quest = userQuest.getQuest();

            if (quest.getQuestConditionType() == QuestConditionType.ATTENDANCE) continue; // 출석체크 퀘스트는 검사하지 않음
            if (userQuest.isClear()) continue; // 이미 클리어 처리 돼있는 퀘스트는 검사하지 않음

            // 그 userQuest의 기간 동안, 최종 결과가 SUCCESS 이면서 문제 유형이 일치하는 것 추출
            List<UserSolvingStatus> validRecordList = ussRepository.findAllByUserIdByPeriod(userQuest.getUser().getId(), userQuest.getStartDate(), userQuest.getEndDate())
                    .stream()
                    .filter(uss -> uss.getSolvingStatus() == SolvingStatus.SUCCESS)
                    .filter(uss -> uss.getProblem().getProblemType().toString().equals(quest.getQuestConditionType().toString()))
                    .collect(Collectors.toList());

            // 해당 개수가 조건 개수보다 많으면 isClear = true 처리
            if (validRecordList.size() >= quest.getQuestConditionNumber())
                userQuestRepository.save(userQuest.toBuilder().isClear(true).build());

        }



    }

}