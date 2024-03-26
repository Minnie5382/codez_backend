package nob.codez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseResponse;
import nob.codez.dto.PostImageFileRes;
import nob.codez.service.S3Service;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

import static nob.codez.config.response.BaseResponseStatus.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Image File", description = "유저 프로필 이미지 파일 등 이미지 파일을 업로드하는 API")
public class FileController {
    private final S3Service s3Service;

    @PostMapping(value = "/users/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "유저 프로필 이미지 변경")
    public BaseResponse<PostImageFileRes> upload(@RequestParam(value = "profileImage") MultipartFile uploadFile, HttpSession session) {
        // 유저 권한 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return new BaseResponse<>(NOT_LOGGED_IN_USER);

        // 이미지 파일인지 확인
        if (!uploadFile.getContentType().startsWith("image/")) {
            return new BaseResponse<>(FILE_NOT_IMAGE);
        }

        try {
            // 파일 업로드
            URL fileUrl = s3Service.uploadFile(uploadFile, userId);

            // 프로필 이미지 URL 업데이트
            s3Service.updateUserProfileImage(userId, fileUrl);

            return new BaseResponse<>(new PostImageFileRes(userId, fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResponse<>(FILE_UPLOAD_FAIL);
        }
    }
}
