package nob.codez.service;

import lombok.RequiredArgsConstructor;
import nob.codez.config.response.BaseException;
import nob.codez.domain.Level;
import nob.codez.domain.User;
import nob.codez.repository.LevelRepository;
import nob.codez.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static nob.codez.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {
    private final LevelRepository levelRepository;
    private final UserRepository userRepository;

    //경험치를 올리고, 필요하면 레벨도 올리는 메서드
    public void expUp(Long userId, int exp) throws BaseException {
        //데이터 가져오기
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) throw new BaseException(RESPONSE_ERROR);
        User user = optionalUser.get();
        Level level = user.getLevel();

        //레벨업 + 경험치업
        if((level.getExpMax() <= user.getExp() + exp) && level.getLevel() < 100){
            Level newLevel = levelRepository.findById(level.getLevel() + 1).get();
            int addExp = user.getExp() + exp - level.getExpMax();
            User newUser = user.toBuilder().level(newLevel).exp(Math.max(addExp, 0)).build();
            userRepository.save(newUser);
        }
        //안레벨업 + 경험치업
        else {
            User newUser = user.toBuilder().exp(user.getExp() + exp).build();
            userRepository.save(newUser);
        }


    }

}
