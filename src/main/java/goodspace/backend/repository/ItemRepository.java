package goodspace.backend.repository;

import goodspace.backend.domain.client.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
