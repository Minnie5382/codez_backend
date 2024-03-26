package nob.codez.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
public class ProfileRequest {
    @Schema(example = "")
    private String password;
    @Schema(example = "나나쀼")
    private String nickname;
    @Schema(example = "고양이가 코딩해도 나보다 낫겠네")
    private String profileIntroduce;
}
