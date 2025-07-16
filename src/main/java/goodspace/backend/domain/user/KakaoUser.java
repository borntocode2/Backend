package goodspace.backend.domain.user;

import jakarta.persistence.Entity;

@Entity
public class KakaoUser extends User {
    private String identifier;
}
