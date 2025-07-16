package goodspace.backend.domain.user;

import jakarta.persistence.Entity;

@Entity
public class GoodSpaceUser extends User {
    private String password;
}
