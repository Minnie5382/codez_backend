package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class SolutionResultResponse {
    private ExecutedResultDTO[] result;
}
