package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nob.codez.enums.Rank;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE LEVEL SET status = 'DELETED' WHERE id = ?")
public class Level extends BaseEntity{
    @Id
    @Column(name = "level_id")
    @Builder.Default
    private Long level = 0L;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Rank rank = Rank.INTERN;
    private int expMax;
}
