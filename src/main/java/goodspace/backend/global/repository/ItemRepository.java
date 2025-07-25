package goodspace.backend.global.repository;

import goodspace.backend.global.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
