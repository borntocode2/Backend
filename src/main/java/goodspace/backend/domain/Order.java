package goodspace.backend.domain;

import goodspace.backend.domain.client.Item;
import goodspace.backend.domain.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item")
    private List<Item> items = new ArrayList<>();
}
