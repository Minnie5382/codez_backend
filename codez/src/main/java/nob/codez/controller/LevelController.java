package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nob.codez.domain.Level;
import nob.codez.enums.Rank;
import nob.codez.repository.LevelRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Level")
public class LevelController {
//    private final LevelRepository levelRepository;

//    /**
//     * 기본 레벨 데이터 (0~100) 생성
//     * @return String
//     */
//    @PostMapping("/admin/levels")
//    @Operation(summary = "(테스트용) 기본 레벨 데이터 (0~50) 생성", description = "애플리케이션 실행 후 최초 1회 요청 필수, 테스트 용이므로 운영 단계에서는 빠질 예정")
//    public String createDefaultLevels() {
//        for (int i = 0; i <= 50; i++) {
//            int maxExp = (int)Math.round(100 * Math.pow(1.1, i));
//            if (i == 0) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.INTERN)
//                        .expMax(20)
//                        .build());
//            }
//            if (1 <= i && i < 10) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.INTERN)
//                        .expMax(maxExp)
//                        .build());
//            }
//            if (10 <= i && i < 20) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.JUNIOR)
//                        .expMax(maxExp)
//                        .build());
//            }
//            if (20 <= i && i < 30) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.SENIOR)
//                        .expMax(maxExp)
//                        .build());
//            }
//            if (30 <= i && i < 40) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.EXPERT)
//                        .expMax(maxExp)
//                        .build());
//            }
//            if (40 <= i && i < 50) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.MASTER)
//                        .expMax(maxExp)
//                        .build());
//            }
//            if (i == 50) {
//                levelRepository.save(Level.builder()
//                        .level((long) i)
//                        .rank(Rank.GOD)
//                        .expMax(Integer.MAX_VALUE)
//                        .build());
//            }
//        }
//
//        return "레벨 0~50 데이터 삽입 완료!";
//    }
}
