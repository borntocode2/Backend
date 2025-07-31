package goodspace.backend.user.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoodSpaceUser extends User {
    private String password;

    public void updatePassword(String password) {
        this.password = password;
    }
}
