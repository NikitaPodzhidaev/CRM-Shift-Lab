package shift.lab.crm.transaction.domain;

import lombok.Getter;
import shift.lab.crm.seller.domain.Seller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaction {

    private final Long id;
    private final Seller seller;
    private final BigDecimal amount;
    private final String paymentType;
    private final LocalDateTime transactionDate;

    public Transaction(Seller seller, BigDecimal amount, String paymentType, LocalDateTime transactionDate){
        this(null, seller, amount, paymentType, transactionDate);
    }

    public Transaction(Long id, Seller seller, BigDecimal amount, String paymentType, LocalDateTime transactionDate) {
        validateSeller(seller);
        validateAmount(amount);
        validatePaymentType(paymentType);
        validateTransactionDate(transactionDate);
        this.id = id;
        this.seller = seller;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }

    private void validateSeller(Seller seller){
        if(seller == null) throw new IllegalArgumentException("Seller must not be null");
    }

    private void validateAmount(BigDecimal amount){
        if(amount == null) throw new IllegalArgumentException("Amount must not be null");
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must not be zero or less than zero");
        }
    }

    private void validatePaymentType(String paymentType){
        try {
            PaymentType.valueOf(paymentType);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Payment type: " + paymentType + " is not valid");
        }
    }
    private void validateTransactionDate(LocalDateTime transactionDate) {
        if(transactionDate == null) throw new IllegalArgumentException("Transaction date must not be null");
    }
}
