package goodspace.backend.client.domain;

import goodspace.backend.global.domain.Item;
import goodspace.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE client SET deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Client extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Setter
    private String profileImageUrl;

    @Setter
    private String backgroundImageUrl;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegisterStatus status;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Item> items = new ArrayList<>();

    public void update(
            String name,
            String introduction,
            ClientType clientType,
            RegisterStatus status
    ) {
        this.name = name;
        this.introduction = introduction;
        this.clientType = clientType;
        this.status = status;
    }

    public boolean isPublic() {
        return status == RegisterStatus.PUBLIC;
    }

    public List<Item> getPublicItems() {
        return items.stream()
                .filter(Item::isPublic)
                .toList();
    }

    /**
     * Client - Item 연관관계 편의 메서드
     */
    public void addItem(Item item) {
        items.add(item);
        item.setClient(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
