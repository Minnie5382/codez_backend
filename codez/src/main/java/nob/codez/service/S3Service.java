package nob.codez.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nob.codez.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {
    private final AmazonS3 amazonS3Client;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public URL uploadFile(MultipartFile uploadFile, Long userId) throws IOException {
        // 버킷에 저장할 객체 이름 생성
        String fileName = UUID.randomUUID() + "_" + uploadFile.getOriginalFilename();

        // 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadFile.getContentType());
        metadata.setContentLength(uploadFile.getSize());

        // 업로드
        amazonS3Client.putObject(bucket, fileName, uploadFile.getInputStream(), metadata);

        // 업로드한 객체 URL 받아오기
        return amazonS3Client.getUrl(bucket, fileName);
    }

    public void updateUserProfileImage(Long userId, URL fileUrl) {
        // db의 유저 정보에 URL 업데이트
        userRepository.findById(userId).ifPresent(
                user -> {
                    userRepository.save(
                            user.toBuilder()
                                    .profileImage(fileUrl.toString())
                                    .build()
                    );
                }
        );
    }
}

