package shift.lab.crm.transaction.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findBySeller_Id(Long sellerId);

}
