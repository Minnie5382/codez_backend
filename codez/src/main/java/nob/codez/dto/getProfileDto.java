package nob.codez.dto;

import lombok.Builder;
import lombok.Data;
import nob.codez.enums.Rank;

@Data
@Builder(toBuilder = true)
public class getProfileDto {
    private Long Id;
    private String password;
    private String nickname;
    private int exp;
    private int maxExp;
    private int level;
    private Rank rank;
    private String profileImage;
    private String profileIntroduce;
    private int submitProblemNum;
    private int successProblemNum;
}
