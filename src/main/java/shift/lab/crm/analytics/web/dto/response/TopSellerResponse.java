package shift.lab.crm.analytics.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TopSellerResponse(
        Long sellerId,
        String sellerName,
        BigDecimal totalAmount,
        LocalDateTime periodStart,
        LocalDateTime periodEnd
) {
}
