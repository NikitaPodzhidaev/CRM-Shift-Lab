package shift.lab.crm.transaction.domain;

import lombok.Getter;
import shift.lab.crm.common.exception.DomainValidationException;
import shift.lab.crm.seller.domain.Seller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaction {

    private final Long id;
    private final Long sellerId;
    private final BigDecimal amount;
    private final String paymentType;
    private final LocalDateTime transactionDate;

    public Transaction(Long sellerId, BigDecimal amount, String paymentType, LocalDateTime transactionDate){
        this(null, sellerId, amount, paymentType, transactionDate);
    }

    public Transaction(Long id, Long sellerId, BigDecimal amount, String paymentType, LocalDateTime transactionDate) {
        validateAmount(amount);
        //validatePaymentType(paymentType);
        validateTransactionDate(transactionDate);
        this.id = id;
        this.sellerId = sellerId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }

    private void validateAmount(BigDecimal amount){
        if(amount == null) throw new DomainValidationException("Amount must not be null");
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Amount must not be zero or less than zero");
        }
    }

    private void validateTransactionDate(LocalDateTime transactionDate) {
        if(transactionDate == null) throw new DomainValidationException("Transaction date must not be null");
    }
}
