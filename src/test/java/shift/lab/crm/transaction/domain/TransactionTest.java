package shift.lab.crm.transaction.domain;

import org.junit.jupiter.api.Test;
import shift.lab.crm.common.exception.DomainValidationException;
import shift.lab.crm.transaction.exception.UnknownPaymentTypeException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void shouldCreateTransactionWithCorrectData() {
        Long sellerId = 8L;
        BigDecimal amount = new BigDecimal("1500.00");
        String paymentType = "CARD";
        LocalDateTime date = LocalDateTime.of(2026, 5, 12, 10, 30);

        Transaction transaction = new Transaction(
                sellerId,
                amount,
                paymentType,
                date
        );

        assertEquals(sellerId, transaction.sellerId());
        assertEquals(0, amount.compareTo(transaction.amount()));
        assertEquals(paymentType, transaction.paymentType());
        assertEquals(date, transaction.transactionDate());
    }

    @Test
    void shouldThrowExceptionWhenSellerIdIsNull() {
        assertThrows(DomainValidationException.class, () ->
                new Transaction(
                        null,
                        new BigDecimal("1500.00"),
                        "CARD",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        assertThrows(DomainValidationException.class, () ->
                new Transaction(
                        8L,
                        null,
                        "CARD",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {
        assertThrows(DomainValidationException.class, () ->
                new Transaction(
                        8L,
                        BigDecimal.ZERO,
                        "CARD",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAmountIsLessThanZero() {
        assertThrows(DomainValidationException.class, () ->
                new Transaction(
                        8L,
                        new BigDecimal("-100.00"),
                        "CARD",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPaymentTypeIsNull() {
        assertThrows(DomainValidationException.class, () ->
                new Transaction(
                        8L,
                        new BigDecimal("1500.00"),
                        null,
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPaymentTypeIsBlank() {
        assertThrows(UnknownPaymentTypeException.class, () ->
                new Transaction(
                        8L,
                        new BigDecimal("1500.00"),
                        "",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPaymentTypeIsUnknown() {
        assertThrows(UnknownPaymentTypeException.class, () ->
                new Transaction(
                        8L,
                        new BigDecimal("1500.00"),
                        "CRYPTO",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenTransactionDateIsNull() {
        assertThrows(DomainValidationException.class, () ->
                new Transaction(
                        8L,
                        new BigDecimal("1500.00"),
                        "CARD",
                        null
                )
        );
    }
}