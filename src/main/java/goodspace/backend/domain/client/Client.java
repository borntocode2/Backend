package goodspace.backend.domain.client;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Client {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String profileImageUrl;
    private byte[] backgrountImage;
    private String introduction;

    @OneToMany(mappedBy = "client")
    private List<Item> items;
}
