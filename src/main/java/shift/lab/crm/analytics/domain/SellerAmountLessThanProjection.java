package shift.lab.crm.analytics.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SellerAmountLessThanProjection {
    Long getSellerId();
    String getSellerName();
    BigDecimal getTotalAmount();
}
