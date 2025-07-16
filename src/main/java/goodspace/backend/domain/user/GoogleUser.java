package goodspace.backend.domain.user;

import jakarta.persistence.Entity;

@Entity
public class GoogleUser extends User {
    private String identifier;
}
