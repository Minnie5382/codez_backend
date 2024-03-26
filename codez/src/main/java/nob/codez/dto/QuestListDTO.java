package nob.codez.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nob.codez.domain.Quest;
import nob.codez.enums.QuestType;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class QuestListDTO {
    private Long id;
    private QuestType type;
    private String name;
    private String description;
    private int exp;
    private boolean isDone;
    private boolean isClear;

    public QuestListDTO(Quest quest, boolean isDone, boolean isClear) {
        this.id = quest.getId();
        this.type = quest.getQuestType();
        this.name = quest.getName();
        this.description = quest.getDescription();
        this.exp = quest.getExp();
        this.isDone = isDone;
        this.isClear = isClear;
    }
}
