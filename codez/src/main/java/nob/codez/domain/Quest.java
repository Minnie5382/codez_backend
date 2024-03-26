package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nob.codez.enums.QuestType;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE QUEST SET status = 'DELETED' WHERE id = ?")
public class Quest extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestType questType;
    private String name;
    private String description;
    private int exp;

    @Enumerated(EnumType.STRING)
    private QuestConditionType questConditionType;
    private int questConditionNumber;

}
