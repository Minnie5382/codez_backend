package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ExecuteProblemRequest {
    Long problemId;
    String code;
    String timeLimit;
}
