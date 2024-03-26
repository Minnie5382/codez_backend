package nob.codez.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

@Data
@AllArgsConstructor
public class PostImageFileRes {
    @Schema(example = "1")
    private Long userId;
    @Schema(example = "https://codez-bucket.s3.ap-northeast-2.amazonaws.com/default_profile_image.png")
    private URL s3url;
}
