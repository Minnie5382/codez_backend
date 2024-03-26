package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nob.codez.enums.Language;
import nob.codez.enums.ResultDetail;
import org.hibernate.annotations.SQLDelete;

import static nob.codez.enums.Language.JAVA;

@Entity @Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE SOLVING_RECORD SET status = 'DELETED' WHERE id = ?")
public class SolvingRecord extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solving_record_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ResultDetail resultDetail;
    @Builder.Default
    private int executionTime = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Language language = JAVA;
    @Builder.Default
    private boolean isCorrect = false;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
