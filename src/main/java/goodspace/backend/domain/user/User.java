package goodspace.backend.domain.user;

import goodspace.backend.domain.Order;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String profileImageUrl;
    private int dateOfBirth;
    private String email;

    @OneToMany(mappedBy = "order")
    private final List<Order> orders = new ArrayList<>();

    @Embedded
    private Delivery delivery;
}


