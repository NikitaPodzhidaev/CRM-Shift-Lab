package shift.lab.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.lab.crm.persistence.SellerEntity;

public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
}
