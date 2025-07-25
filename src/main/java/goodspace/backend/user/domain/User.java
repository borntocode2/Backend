package goodspace.backend.user.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.order.domain.Order;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.global.security.RefreshToken;
import goodspace.backend.user.dto.UserMyPageDto;
import goodspace.backend.global.security.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Table(name = "`user`")
public abstract class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer dateOfBirth;
    @Column(unique = true, nullable = false)
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCartItem> userCartItems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Question> question = new ArrayList<>();

    @Embedded
    private Delivery delivery;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    public void updateRefreshToken(String refreshTokenValue) {
        if (this.refreshToken == null) {
            this.refreshToken = new RefreshToken(refreshTokenValue);
            return;
        }

        this.refreshToken.setTokenValue(refreshTokenValue);
    }

    public void addRole(Role role) {
        UserRole userRole = UserRole.builder()
                .role(role)
                .user(this)
                .build();

        roles.add(userRole);
    }

    public List<Role> getRoles() {
        return roles.stream()
                .map(UserRole::getRole)
                .toList();
    }

    public String getRefreshToken() {
        if (refreshToken == null) {
            return "";
        }

        return this.refreshToken.getTokenValue();
    }

    public User setUserFromUserMyPageDto(UserMyPageDto userMyPageDto){
        this.email = userMyPageDto.getEmail();
        this.phoneNumber = userMyPageDto.getPhoneNumber();
        this.name = userMyPageDto.getName();
        this.dateOfBirth = userMyPageDto.getDateOfBirth();
        this.delivery = delivery.from(userMyPageDto);

        return this;
    }
}
