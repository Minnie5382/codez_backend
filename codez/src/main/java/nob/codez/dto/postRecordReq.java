package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class postRecordReq {
    private Long userId;
    private Long problemId;
    private boolean isCorrect;
}