package goodspace.backend.domain.client;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @GeneratedValue
    private Long id;

    private String name;
    private String profileImageUrl;
    private String backgroundImageUrl;
    private String introduction;
    private ClientType clientType;

    @OneToMany(mappedBy = "client")
    @Builder.Default
    private final List<Item> items = new ArrayList<>();

    /**
     * Client - Item 연관관계 편의 메서드
     */
    public void addItem(Item item) {
        items.add(item);
        item.setClient(this);
    }
}
