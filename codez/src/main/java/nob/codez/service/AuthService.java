package nob.codez.service;

import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.config.response.BaseResponse;
import nob.codez.domain.Level;
import nob.codez.domain.User;
import nob.codez.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static nob.codez.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;

    // 회원가입
    public User addUser(String email, String password, String nickname) throws BaseException {
        // 중복 이메일 검사
        if (isDuplicatedEmail(email))
            throw new BaseException(USER_DUPLICATED_EMAIL);
        // 중복 닉네임 검사
        if (isDuplicatedNickname(nickname))
            throw new BaseException(USER_DUPLICATED_NICKNAME);

        String encryptedPw = EncryptPw(password);

        User user = User.builder()
                .email(email)
                .password(encryptedPw)
                .profileImage("https://codez-bucket.s3.ap-northeast-2.amazonaws.com/default_profile_image.png") // 기본 프로필 이미지
                .nickname(nickname)
                .exp(0)
                .level(Level.builder().build())
                .build();

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    // 로그인
    public User login(String email, String password) throws BaseException {
        Optional<User> findUserByEmail = userRepository.findByEmail(email);
        // 유저가 없다면
        if (findUserByEmail.isEmpty()) throw new BaseException(INVALID_ID_PASSWORD);

        // 비밀번호가 일치하면
        if (EncryptPw(password).equals(findUserByEmail.get().getPassword())) {
            return findUserByEmail.get();
        } else { // 일치하지 않으면
            throw new BaseException(INVALID_ID_PASSWORD);
        }
    }

    public boolean isDuplicatedEmail(String email) {
        List<User> allByEmail = userRepository.findAllByEmail(email);
        if (allByEmail.size() == 0) return false;
        else return true;
    }

    public boolean isDuplicatedNickname(String nickname) {
        List<User> allByNickname = userRepository.findAllByNickname(nickname);
        if (allByNickname.size() == 0) return false;
        else return true;
    }

    public String EncryptPw (String rawPassword) {
        // Salt 생성
        byte[] salt = generateSalt();

        // 비밀번호를 해싱하여 암호화
//        String hashedPassword = hashPassword(rawPassword, salt);
        String hashedPassword = hashPassword(rawPassword);
        return hashedPassword;
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // 비밀번호 암호화 : 솔트 있는 버전
    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Base64 인코딩하여 저장
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 비밀번호 해싱 : 솔트 없는 버전
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());

            // Base64 인코딩하여 저장
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
