package goodspace.backend.user.repository;

import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.domain.OAuthType;
import goodspace.backend.user.domain.OAuthUser;
import goodspace.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM GoodSpaceUser u WHERE u.id = :id")
    Optional<GoodSpaceUser> findGoodSpaceUserById(long id);

    @Query("SELECT u FROM GoodSpaceUser u WHERE u.email = :email AND u.password = :password")
    Optional<GoodSpaceUser> findByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM OAuthUser u WHERE u.identifier = :identifier AND u.oauthType = :oauthType")
    Optional<OAuthUser> findByIdentifierAndOAuthType(
            @Param("identifier") String identifier,
            @Param("oauthType") OAuthType oauthType
    );
}
