package nob.codez.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nob.codez.enums.Status;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static nob.codez.enums.Status.ACTIVE;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "status = 'ACTIVE'")
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = ACTIVE;

}
