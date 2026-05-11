package shift.lab.crm.transaction.web.dto.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long sellerId,
        BigDecimal amount,
        String paymentType,
        LocalDateTime transactionDate

) {
}
