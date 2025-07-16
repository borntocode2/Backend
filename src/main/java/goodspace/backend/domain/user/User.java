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
    private String profile_image_url;
    private int date_of_birth;

    @OneToMany(mappedBy = "order")
    private final List<Order> orders = new ArrayList<>();

    @Embedded
    private Delivery delivery;
}


