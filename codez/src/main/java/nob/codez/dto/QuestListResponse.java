package nob.codez.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestListResponse {
    private List<QuestListDTO> questList;
}
