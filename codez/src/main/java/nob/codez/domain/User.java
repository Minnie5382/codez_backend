package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE USERS SET status = 'DELETED' WHERE id = ?")
@Table(name = "Users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private int exp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;
    @Column(name = "profile_image")
    @Lob
    private String profileImage;
    @Column(name = "profile_introduce")
    @Builder.Default
    private String profileIntroduce = "";

    //출석 퀘스트를 위한 출석 체크 필드
//    @Builder.Default
//    private boolean isAttending = false;
}
