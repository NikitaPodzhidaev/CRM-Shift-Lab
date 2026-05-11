package shift.lab.crm.transaction.domain;

import shift.lab.crm.common.exception.DomainValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(Long id, Long sellerId, BigDecimal amount, String paymentType,
                          LocalDateTime transactionDate) {

    public Transaction(Long sellerId, BigDecimal amount, String paymentType, LocalDateTime transactionDate) {
        this(null, sellerId, amount, paymentType, transactionDate);
    }

    public Transaction {
        validateAmount(amount);
        //validatePaymentType(paymentType);
        validateTransactionDate(transactionDate);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) throw new DomainValidationException("Amount must not be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Amount must not be zero or less than zero");
        }
    }

    private void validateTransactionDate(LocalDateTime transactionDate) {
        if (transactionDate == null) throw new DomainValidationException("Transaction date must not be null");
    }
}
