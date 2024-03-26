package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import java.time.LocalDate;


@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE USER_QUEST SET status = 'DELETED' WHERE id = ?")
public class UserQuest extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_quest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column (name = "is_done")
    @Builder.Default
    private boolean isDone = false;

    @Column (name = "is_clear")
    @Builder.Default
    private boolean isClear = false;

    @Column (name = "start_date")
    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Column (name = "end_date")
    @Builder.Default
    private LocalDate endDate = LocalDate.now();
}
