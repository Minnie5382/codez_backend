package nob.codez.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    @Schema(example = "user@example.com")
    private String email;
    @Schema(example = "123456789Z")
    private String password;
    @Schema(example = "불꽃코더")
    private String nickname;
}
