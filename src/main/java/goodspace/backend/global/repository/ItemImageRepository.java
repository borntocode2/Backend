package goodspace.backend.global.repository;

import goodspace.backend.global.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
