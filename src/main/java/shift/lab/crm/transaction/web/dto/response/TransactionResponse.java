package shift.lab.crm.transaction.web.dto.response;


import shift.lab.crm.seller.domain.Seller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Seller seller,
        BigDecimal amount,
        String paymentType,
        LocalDateTime transactionDate

) {
}
