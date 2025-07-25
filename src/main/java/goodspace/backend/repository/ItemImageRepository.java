package goodspace.backend.repository;

import goodspace.backend.domain.client.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
