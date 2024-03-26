package nob.codez.dto;

import lombok.*;
import nob.codez.enums.Difficulty;
import nob.codez.enums.ProblemType;
import nob.codez.enums.SolvingStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemInfo {
    private Long problemId;
    private String title;
    private SolvingStatus solvingStatus;
    private int correctRate;
    private Difficulty difficulty;
    private int submitNum;
    private ProblemType type;

}
