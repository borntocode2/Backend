package goodspace.backend.domain.user;

import goodspace.backend.domain.Order;
import goodspace.backend.dto.UserMyPageDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer dateOfBirth;
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private final List<Order> orders = new ArrayList<>();

    @Embedded
    private Delivery delivery;

    public User setUserFromUserMyPageDto(UserMyPageDto userMyPageDto){
        this.email = userMyPageDto.getEmail();
        this.phoneNumber = userMyPageDto.getPhoneNumber();
        this.name = userMyPageDto.getName();
        this.dateOfBirth = userMyPageDto.getDateOfBirth();
        this.delivery = delivery.from(userMyPageDto);

        return this;
    }
}
