package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nob.codez.enums.Difficulty;
import nob.codez.enums.PostingStatus;
import nob.codez.enums.ProblemType;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE PROBLEM SET status = 'DELETED' WHERE id = ?")
public class Problem extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;
    private String title;

    @Lob
    private String content;
    private int timeLimit;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Lob
    private String inputFormatJava;
    private int exp;

    @Enumerated(EnumType.STRING)
    private PostingStatus postingStatus;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name="problem_correct_stat_id")
    private ProblemCorrectStat problemCorrectStat;
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;
}
