package nob.codez.service;

import lombok.AllArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.domain.User;
import nob.codez.dto.ProfileResponse;
import nob.codez.dto.ProfileRequest;
import nob.codez.dto.RankingResponse;
import nob.codez.dto.getProfileDto;
import nob.codez.repository.LevelRepository;
import nob.codez.repository.UserRepository;
import nob.codez.repository.UserSolvingStatus.UserSolvingStatusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static nob.codez.config.response.BaseResponseStatus.*;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private AuthService authService;
    private LevelRepository levelRepository;
    private UserSolvingStatusRepository ussRepository;

    // 회원 등록
    public User addUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public ProfileResponse editUser(Long userId, ProfileRequest userEdit) throws BaseException {
        User foundUser = userRepository.findById(userId).orElse(null);

        User updatedUser = foundUser.toBuilder()
                .password(userEdit.getPassword() != null ?
                        authService.EncryptPw(userEdit.getPassword()) : foundUser.getPassword())
                .nickname(userEdit.getNickname() != null ?
                        userEdit.getNickname() : foundUser.getNickname())
                .profileIntroduce(userEdit.getProfileIntroduce() != null ?
                        userEdit.getProfileIntroduce() : foundUser.getProfileIntroduce())
                .build();

        userRepository.save(updatedUser);

        return transferProfileDto(foundUser);
    }

    public getProfileDto getUser(Long userId) throws BaseException {
        User foundUser = userRepository.findById(userId).orElse(null);

        if (foundUser == null) {
            throw new BaseException(INVALID_USER);
        }

        return transfergetProfileDto(foundUser);
    }

    //User 조회 dto로 변환
    private getProfileDto transfergetProfileDto(User user){
        return getProfileDto.builder()
                .Id(user.getId())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .exp(user.getExp())
                .maxExp(user.getLevel().getExpMax())
                .level(user.getLevel().getLevel().intValue())
                .rank(user.getLevel().getRank())
                .profileImage(user.getProfileImage())
                .profileIntroduce(user.getProfileIntroduce())
                .submitProblemNum(ussRepository.successSubmitNum(user.getId()))
                .successProblemNum(ussRepository.successSubmitNum(user.getId()))
                .build();
    }

    //User를 ProfileDto로 변환
    private ProfileResponse transferProfileDto(User foundUser) {
        return ProfileResponse.builder()
                .id(foundUser.getId())
                .password(foundUser.getPassword())
                .nickname(foundUser.getNickname())
                .exp(foundUser.getExp())
                .level(foundUser.getLevel().getLevel())
                .rank(foundUser.getLevel().getRank())
                .profileImage(foundUser.getProfileImage())
                .profileIntroduce(foundUser.getProfileIntroduce())
                .build();
    }

    //랭킹순으로 전부 받아오기
    public Page<RankingResponse> findRanker(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> rankPage = userRepository.findRankAll(pageable);

        AtomicInteger ranking = new AtomicInteger(pageSize * pageNumber + 1); // 순위를 위한 AtomicInteger 시작값 1로 설정

        List<RankingResponse> result = rankPage.stream()
                .map(user -> makeRanker(user, ranking.getAndIncrement())) // 각 User를 RankingResponse로 변환
                .collect(Collectors.toList());

        return new PageImpl<>(result, pageable, rankPage.getTotalElements());
    }

    private RankingResponse makeRanker(User user, int ranking){
        return RankingResponse.builder()
                .nickname(user.getNickname())
                .profileImg(user.getProfileImage())
                .exp(user.getExp())
                .level(user.getLevel().getLevel())
                .rank(user.getLevel().getRank())
                .ranking(ranking)
                .build();
    }

    //다양한 레벨의 유저 생성하기
    public void createRanker(){
        for (int i = 0; i < 50; i++) {
            User user = User.builder()
                    .password("1234")
                    .nickname(i + "레벨랭커")
                    .exp(100 - i*2)
                    .profileImage("https://codez-bucket.s3.ap-northeast-2.amazonaws.com/default_profile_image.png")
                    .level(levelRepository.findById((long) i).get())
                    .profileIntroduce("")
                    .build();
            userRepository.save(user);
        }
        for (int i = 0; i < 5; i++) {
        User user = User.builder()
                .password("1234")
                .nickname("50레벨랭커(같은레벨다른경험치)")
                .exp(55 + i)
                .profileImage("https://codez-bucket.s3.ap-northeast-2.amazonaws.com/default_profile_image.png")
                .level(levelRepository.findById(50L).get())
                .profileIntroduce("")
                .build();
        userRepository.save(user);

        }
    }

//    //로그인하면 출석되는 메서드
//    public void checkAttending(Long userId){
//        User user = userRepository.findById(userId).orElse(null);
//        user = user.toBuilder()
//                .isAttending(true)
//                .build();
//        userRepository.save(user);
//    }
//
//    //매일 자정마다 출석 초기화 메서드
//    @Scheduled(cron = "0 0 0 * * *")
//    public void resetIsAttending(){
//        List<User> user = userRepository.findAll();
//        for (int i = 0; i < user.size(); i++) {
//           user.get(i).toBuilder()
//                   .isAttending(false)
//                   .build();
//            userRepository.save(user.get(i));
//        }
//    }
}
