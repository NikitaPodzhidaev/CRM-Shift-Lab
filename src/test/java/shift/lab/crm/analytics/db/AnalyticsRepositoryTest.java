package shift.lab.crm.analytics.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import shift.lab.crm.analytics.domain.SellerAmountLessThanProjection;
import shift.lab.crm.analytics.domain.TopSellerProjection;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.db.SellerRepository;
import shift.lab.crm.transaction.db.TransactionEntity;
import shift.lab.crm.transaction.db.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class AnalyticsRepositoryTest {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void shouldFindTopSellerByPeriod() {
        SellerEntity firstSeller = createSeller("Ivan");
        SellerEntity secondSeller = createSeller("Anna");

        transactionRepository.save(createTransaction(
                firstSeller,
                new BigDecimal("1000.00"),
                LocalDateTime.of(2026, 5, 10, 10, 0)
        ));

        transactionRepository.save(createTransaction(
                firstSeller,
                new BigDecimal("2000.00"),
                LocalDateTime.of(2026, 5, 10, 12, 0)
        ));

        transactionRepository.save(createTransaction(
                secondSeller,
                new BigDecimal("7000.00"),
                LocalDateTime.of(2026, 5, 10, 14, 0)
        ));

        List<TopSellerProjection> result = analyticsRepository.findTopSellerByPeriod(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 6, 1, 0, 0),
                PageRequest.of(0, 1)
        );

        assertEquals(1, result.size());

        TopSellerProjection topSeller = result.getFirst();

        assertEquals(secondSeller.getId(), topSeller.getSellerId());
        assertEquals("Anna", topSeller.getSellerName());
        assertEquals(0, new BigDecimal("7000.00").compareTo(topSeller.getTotalAmount()));
    }

    @Test
    void shouldNotIncludeTransactionAtEndPeriod() {
        SellerEntity seller = createSeller("Ivan");

        transactionRepository.save(createTransaction(
                seller,
                new BigDecimal("1000.00"),
                LocalDateTime.of(2026, 5, 31, 23, 59)
        ));

        transactionRepository.save(createTransaction(
                seller,
                new BigDecimal("9999.00"),
                LocalDateTime.of(2026, 6, 1, 0, 0)
        ));

        List<TopSellerProjection> result = analyticsRepository.findTopSellerByPeriod(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 6, 1, 0, 0),
                PageRequest.of(0, 1)
        );

        assertEquals(1, result.size());

        TopSellerProjection topSeller = result.getFirst();

        assertEquals(seller.getId(), topSeller.getSellerId());
        assertEquals(0, new BigDecimal("1000.00").compareTo(topSeller.getTotalAmount()));
    }

    @Test
    void shouldFindSellersWithTotalAmountLessThanByPeriod() {
        SellerEntity firstSeller = createSeller("Ivan");
        SellerEntity secondSeller = createSeller("Anna");
        SellerEntity thirdSeller = createSeller("Oleg");

        transactionRepository.save(createTransaction(
                firstSeller,
                new BigDecimal("3000.00"),
                LocalDateTime.of(2026, 5, 10, 10, 0)
        ));

        transactionRepository.save(createTransaction(
                secondSeller,
                new BigDecimal("15000.00"),
                LocalDateTime.of(2026, 5, 10, 10, 0)
        ));

        transactionRepository.save(createTransaction(
                thirdSeller,
                new BigDecimal("5000.00"),
                LocalDateTime.of(2026, 5, 10, 10, 0)
        ));

        List<SellerAmountLessThanProjection> result =
                analyticsRepository.findSellersWithTotalAmountLessThanByPeriod(
                        LocalDateTime.of(2026, 5, 1, 0, 0),
                        LocalDateTime.of(2026, 6, 1, 0, 0),
                        new BigDecimal("10000.00")
                );

        List<Long> sellerIds = result.stream()
                .map(SellerAmountLessThanProjection::getSellerId)
                .toList();

        assertTrue(sellerIds.contains(firstSeller.getId()));
        assertTrue(sellerIds.contains(thirdSeller.getId()));
        assertFalse(sellerIds.contains(secondSeller.getId()));
    }

    @Test
    void shouldIncludeSellerWithZeroTransactionsInLessThanQuery() {
        SellerEntity sellerWithTransactions = createSeller("Ivan");
        SellerEntity sellerWithoutTransactions = createSeller("Anna");

        transactionRepository.save(createTransaction(
                sellerWithTransactions,
                new BigDecimal("5000.00"),
                LocalDateTime.of(2026, 5, 10, 10, 0)
        ));

        List<SellerAmountLessThanProjection> result =
                analyticsRepository.findSellersWithTotalAmountLessThanByPeriod(
                        LocalDateTime.of(2026, 5, 1, 0, 0),
                        LocalDateTime.of(2026, 6, 1, 0, 0),
                        new BigDecimal("10000.00")
                );

        List<Long> sellerIds = result.stream()
                .map(SellerAmountLessThanProjection::getSellerId)
                .toList();

        assertTrue(sellerIds.contains(sellerWithTransactions.getId()));
        assertTrue(sellerIds.contains(sellerWithoutTransactions.getId()));

        SellerAmountLessThanProjection zeroSeller = result.stream()
                .filter(seller -> seller.getSellerId().equals(sellerWithoutTransactions.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(0, BigDecimal.ZERO.compareTo(zeroSeller.getTotalAmount()));
    }

    private SellerEntity createSeller(String name) {
        SellerEntity seller = SellerEntity.builder()
                .name(name)
                .contactInfo(name.toLowerCase() + "@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 1, 10, 0))
                .deleted(false)
                .build();

        return sellerRepository.save(seller);
    }

    private TransactionEntity createTransaction(
            SellerEntity seller,
            BigDecimal amount,
            LocalDateTime transactionDate
    ) {
        return new TransactionEntity(
                null,
                seller,
                amount,
                "CARD",
                transactionDate
        );
    }
}