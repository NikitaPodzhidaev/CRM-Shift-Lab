package shift.lab.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.lab.crm.persistence.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
