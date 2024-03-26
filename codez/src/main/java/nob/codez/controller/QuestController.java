package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nob.codez.service.QuestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Quest")
public class QuestController {
//    private final QuestService questService;
//
//    /**
//     * 퀘스트 기본데이터 삽입
//     * @return
//     */
//    @PostMapping("/admin/quests")
//    @Operation(summary = "(테스트용) 퀘스트 기본 데이터 (일일 8개, 주간 7개) 생성", description = "애플리케이션 실행 후 최초 1회 실행 필수, 운영 단계에서는 빠질 예정")
//    public String createDefaultQuests() {
//        questService.insertQuests();
//        return "일일 퀘스트 8개, 주간 퀘스트 7개 생성 완료!";
//    }
}
