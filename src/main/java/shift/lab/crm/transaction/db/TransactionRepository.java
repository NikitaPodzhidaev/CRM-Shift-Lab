package shift.lab.crm.transaction.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.seller
            """)
    List<TransactionEntity> findAllWithSeller();

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.seller
            WHERE t.seller.id = :sellerId
            """)
    List<TransactionEntity> findBySellerIdWithSeller(Long sellerId);

}
