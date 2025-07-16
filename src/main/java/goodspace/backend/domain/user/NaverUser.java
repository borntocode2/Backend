package goodspace.backend.domain.user;

import jakarta.persistence.Entity;

@Entity
public class NaverUser extends User {
    private String identifier;
}
