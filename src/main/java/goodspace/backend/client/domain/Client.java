package goodspace.backend.client.domain;

import goodspace.backend.global.domain.Item;
import goodspace.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Client extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Setter
    private String profileImageUrl;
    @Setter
    private String backgroundImageUrl;
    @Column(nullable = false)
    private String introduction;
    @Column(nullable = false)
    private ClientType clientType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Item> items = new ArrayList<>();

    public void update(
            String name,
            String introduction,
            ClientType clientType
    ) {
        this.name = name;
        this.introduction = introduction;
        this.clientType = clientType;
    }

    /**
     * Client - Item 연관관계 편의 메서드
     */
    public void addItem(Item item) {
        items.add(item);
        item.setClient(this);
    }

    public void addItems(List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setClient(null);
    }
}
