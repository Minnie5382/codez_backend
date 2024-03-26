package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import nob.codez.domain.User;
import nob.codez.service.AuthService;
import nob.codez.dto.AuthResponse;
import nob.codez.dto.LoginRequest;
import nob.codez.dto.RegisterRequest;
import nob.codez.config.response.BaseException;
import nob.codez.config.response.BaseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nob.codez.config.response.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "유저 회원가입, 로그인 등 인증과 관련된 API")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원 가입
     *
     * @param registerRequest
     * @return
     */
    @PostMapping("/api/auth/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<BaseResponse<?>> register(@RequestBody RegisterRequest registerRequest) {
        String nickname = registerRequest.getNickname();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        try {
            User registeredUser = authService.addUser(email, password, nickname);
            AuthResponse authResponse = new AuthResponse(registeredUser.getId());
            return new BaseResponse<>(authResponse).toResponseEntity();

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus()).toResponseEntity();
        }
    }

    /**
     * 로그인
     *
     * @param loginRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/api/auth/login")
    @Operation(summary = "로그인")
    public ResponseEntity<BaseResponse<?>> login(@RequestBody LoginRequest loginRequest,
                                            HttpSession session) {
        // 이미 로그인한 세션인지?
        if (session.getAttribute("userId") != null)
            return new BaseResponse<>(ALREADY_LOGGED_IN_USER).toResponseEntity();

        try {
            User loginedUser = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            session.setAttribute("userId", loginedUser.getId());
            // 응답 객체 생성
            AuthResponse authResponse = new AuthResponse(loginedUser.getId());

            return new BaseResponse<>(authResponse).toResponseEntity();
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus()).toResponseEntity();
        }
    }

    /**
     * 현재 세션 로그아웃
     *
     * @param request
     * @return
     */
    @PostMapping("/api/auth/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<BaseResponse<?>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) // 세션이 있다면
            session.invalidate(); // 그 세션을 무효화!

        return new BaseResponse<>("로그아웃 성공").toResponseEntity();

    }

    /**
     * 로그인 한 유저인지? 확인하는 API
     * @param session
     * @return
     */
    @GetMapping("/admin/check")
    @Operation(summary = "권한 확인", description = "(내부용) 로그인 한 유저인지 확인하는 API")
    public ResponseEntity<BaseResponse<?>> testAuth(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new BaseResponse<>(NOT_LOGGED_IN_USER, HttpStatus.UNAUTHORIZED).toResponseEntity();
        }

        return new BaseResponse<>((userId + "번 유저로 로그인 된 상태입니다.")).toResponseEntity();
    }
}