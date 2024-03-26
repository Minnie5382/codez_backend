package nob.codez.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @Schema(example = "user@example.com")
    private String email;
    @Schema(example = "123456789Z")
    private String password;
}
