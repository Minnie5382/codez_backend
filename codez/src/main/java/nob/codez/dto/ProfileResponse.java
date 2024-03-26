package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nob.codez.domain.Level;
import nob.codez.enums.Rank;

@Getter
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;
    private String password;
    private String nickname;
    private int exp;
    private Long level;
    private Rank rank;
    private String profileImage;
    private String profileIntroduce;
}
