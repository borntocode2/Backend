package goodspace.backend.order.repository;

import goodspace.backend.order.domain.OrderPaymentIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentIssueRepository extends JpaRepository<OrderPaymentIssue, Long> {
}
