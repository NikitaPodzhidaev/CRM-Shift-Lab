package shift.lab.crm.domain;

import java.time.LocalDateTime;

public class Transaction {

    private Long id;
    private Seller seller;
    private double amount;
    private String paymentType;
    private LocalDateTime transactionDate;

}
