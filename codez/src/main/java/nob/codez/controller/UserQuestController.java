package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.config.response.BaseResponse;
import nob.codez.dto.DoneQuestsRequest;
import nob.codez.dto.QuestListResponse;
import nob.codez.dto.QuestListDTO;

import nob.codez.service.UserQuestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static nob.codez.config.response.BaseResponseStatus.NOT_LOGGED_IN_USER;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@Tag(name = "User Quest", description = "유저 퀘스트 완료 처리 등 API")
public class UserQuestController {
    private final UserQuestService userQuestService;

    /**
     * 유저의 일일/주간 퀘스트 목록 조회 API
     * @param session
     * @return
     */
    @GetMapping("/api/quests")
    @Operation(summary = "유저 퀘스트 목록 조회", description = "일일/주간 퀘스트 모두 조회")
    public BaseResponse<QuestListResponse> findAllQuests(HttpSession session){
        //로그인 했는지 검증
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        try {
            //퀘스트 리스트 가져오기
            List<QuestListDTO> quests = userQuestService.findAllQuests(userId);

            // 응답 객체 생성
            QuestListResponse allQuestList = new QuestListResponse(quests);

            return new BaseResponse<>(allQuestList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    /**
     * 퀘스트 완료 처리 + 완료 처리된 결과 조회 API
     * @param session
     * @param quests
     * @return
     */
    @PostMapping("/api/quests/complete")
    @Operation(summary = "퀘스트 완료 처리", description = "퀘스트 완료 처리 후 처리된 결과를 조회함.")
    public BaseResponse<List<Long>> findCompleteQuests (HttpSession session, @RequestBody DoneQuestsRequest quests){
        //로그인 했는지 검증
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        try {
            List<Long> doneQuestIds = userQuestService.getQuestsDone(userId, quests.getQuestIdList());
            return new BaseResponse<>(doneQuestIds);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }


    /**
     * (테스트용) 유저에게 랜덤 퀘스트 할당 API
     * @param session
     * @return
     */
    @PostMapping("/admin/quests/assign")
    @Operation(summary = "(테스트용) 유저 랜덤 퀘스트 할당", description = "애플리케이션 실행 후 최초 1회 요청 필수, 테스트 용이므로 운영 단계에서는 빠질 예정")
    public String assignRandomQuestToUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) return "로그인 먼저 하세요.";

        userQuestService.insertRandomQuestToUser(userId);

        return "유저에데 랜덤 퀘스트 할당됨!";
    }

}
