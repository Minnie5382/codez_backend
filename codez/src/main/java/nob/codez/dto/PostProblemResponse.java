package nob.codez.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class PostProblemResponse {
    private Boolean problemIsCorrect;

}