package shift.lab.crm.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transactions")
@Entity
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private String paymentType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

}
