package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nob.codez.enums.Rank;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class RankingResponse {
    private String nickname;
    private String profileImg;
    private int exp;
    private Long level;
    private Rank rank;
    private int ranking;
}
