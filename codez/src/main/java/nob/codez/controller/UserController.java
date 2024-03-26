package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.config.response.BaseResponse;
import nob.codez.dto.ProfileResponse;
import nob.codez.dto.ProfileRequest;
import nob.codez.dto.RankingResponse;
import nob.codez.dto.getProfileDto;
import nob.codez.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static nob.codez.config.response.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저 프로필 관련 API")
public class UserController {

    private final UserService userService;

    /**
     * 유저 프로필 조회 API
     * @param userId
     * @param loginUserId
     * @return
     */
    @GetMapping("/{userId}/profile")
    @Operation(summary = "유저 프로필 조회")
    @Parameters({@Parameter(name = "userId", description = "유저 id", example = "1")})
    public BaseResponse<getProfileDto> viewProfile(@PathVariable("userId") Long userId,
                                                     @SessionAttribute("userId") Long loginUserId) {
        if (loginUserId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        try {
            getProfileDto foundUser = userService.getUser(userId);
            return new BaseResponse<>(foundUser);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 유저 프로필 수정 API
     * @param httpSession
     * @param userEditRequest
     * @return
     */
    @PostMapping("/profile/edit")
    @Operation(summary = "유저 프로필 수정", description = "프로필 이미지 제외")
    public BaseResponse<ProfileResponse> editProfile(HttpSession httpSession,
                                                     @RequestBody ProfileRequest userEditRequest) {
        //로그인 했는지 검증
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        try {
            ProfileResponse updatedUser = userService.editUser(userId, userEditRequest);

            return new BaseResponse<>(updatedUser);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/dummies")
    @Operation(summary = "(테스트용) 유저 55개 데이터 생성", description = "애플리케이션 실행 후 최초 1회 실행 필수, 운영 단계에서는 빠질 예정")
    public String registerDummy(){
        userService.createRanker();
        return "유저 55명 생성 완료!~~";
    }

    @GetMapping("/rankings")
    public BaseResponse<Page<RankingResponse>> viewRanking(HttpSession session, @RequestParam int page, @RequestParam int size){
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        Page<RankingResponse> rankingPage = userService.findRanker(page, size);

        return new BaseResponse<>(rankingPage);
    }

}
