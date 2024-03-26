package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity @Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE PROBLEM_CORRECT_STAT SET status = 'DELETED' WHERE id = ?")
public class ProblemCorrectStat extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_correct_stat_id")
    private Long id;


    @Builder.Default
    private int correctRate = 0;

    @Builder.Default
    private int submitNum = 0;

    @Builder.Default
    private int correctNum = 0;
}
