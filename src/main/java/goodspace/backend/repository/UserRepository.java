package goodspace.backend.repository;

import goodspace.backend.domain.user.GoodSpaceUser;
import goodspace.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM GoodSpaceUser u WHERE u.email = :email AND u.password = :password")
    Optional<GoodSpaceUser> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);
}
