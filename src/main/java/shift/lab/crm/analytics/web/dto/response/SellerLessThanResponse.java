package shift.lab.crm.analytics.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SellerLessThanResponse(
        Long sellerId,
        String sellerName,
        BigDecimal totalAmount,
        BigDecimal comparedToAmount,
        LocalDateTime periodStart,
        LocalDateTime periodEnd
) {
}
