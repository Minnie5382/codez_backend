package nob.codez.repository;
import nob.codez.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByEmail(String email);

    List<User> findAllByNickname(String nickname);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u FROM User u JOIN u.level l WHERE u.status <> 'DELETED' ORDER BY l.level DESC, u.exp DESC",
            countQuery = "SELECT count(u) FROM User u WHERE u.status <> 'DELETED'")
    Page<User> findRankAll(Pageable pageable);

}
