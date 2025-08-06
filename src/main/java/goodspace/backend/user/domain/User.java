package goodspace.backend.user.domain;

import goodspace.backend.cart.domain.CartItem;
import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.order.domain.Order;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.global.security.RefreshToken;
import goodspace.backend.user.dto.UserMyPageDto;
import goodspace.backend.global.security.Role;
import jakarta.persistence.*;
import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer dateOfBirth;
    @Column(unique = true, nullable = false)
    @Setter
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    // TODO 이후에 이름을 questions로 수정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
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

    public void setUserFromUserMyPageDto(UserMyPageDto userMyPageDto){
        this.phoneNumber = userMyPageDto.getPhoneNumber();
        this.name = userMyPageDto.getName();
        this.dateOfBirth = userMyPageDto.getDateOfBirth();
        this.delivery = Delivery.from(userMyPageDto);
    }

    /**
     * User - Question 연관관계 편의 메서드
     */
    public void addQuestion(Question question) {
        this.question.add(question);
        question.setUser(this);
    }

    /**
     * User - CartItem 연관관계 편의 메서드
     */
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setUser(this);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setUser(null);
    }
}
