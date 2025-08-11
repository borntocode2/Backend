package goodspace.backend.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE good_space_user " +
        "SET password = CONCAT('DELETED_', password) " +
        "WHERE id = ?")
public class GoodSpaceUser extends User {
    @Column(nullable = false)
    private String password;

    public void updatePassword(String password) {
        this.password = password;
    }
}
