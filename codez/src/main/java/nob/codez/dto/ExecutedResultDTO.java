package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ExecutedResultDTO {
    int testNum;
    String input;
    String output;
    String result;
    Long elapsedTime;
}
