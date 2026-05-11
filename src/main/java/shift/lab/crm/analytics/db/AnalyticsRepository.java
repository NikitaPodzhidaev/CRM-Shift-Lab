package shift.lab.crm.analytics.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import shift.lab.crm.analytics.domain.SellerAmountLessThanProjection;
import shift.lab.crm.analytics.domain.TopSellerProjection;
import shift.lab.crm.transaction.db.TransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface AnalyticsRepository extends CrudRepository<TransactionEntity, Long> {


    @Query("""
            SELECT t.seller.id as sellerId,
                   t.seller.name as sellerName,
                   SUM(t.amount) as totalAmount
            FROM TransactionEntity t
            WHERE t.transactionDate >= :startTime
            AND t.transactionDate < :endTime
            GROUP BY t.seller.id, t.seller.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<TopSellerProjection> findTopSellerByPeriod(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);


    @Query("""
       SELECT s.id AS sellerId,
              s.name AS sellerName,
              COALESCE(SUM(t.amount), 0) AS totalAmount
       FROM SellerEntity s
       LEFT JOIN TransactionEntity t
       ON t.seller.id = s.id
       AND t.transactionDate >= :startTime
       AND t.transactionDate < :endTime
       GROUP BY s.id, s.name
       HAVING COALESCE(SUM(t.amount), 0) < :amount
       ORDER BY COALESCE(SUM(t.amount), 0) DESC
       """)
    List<SellerAmountLessThanProjection> findSellersWithTotalAmountLessThanByPeriod(LocalDateTime startTime,
                                                                                    LocalDateTime endTime,
                                                                                    BigDecimal amount);
}
