package shift.lab.crm.analytics.domain;

import java.math.BigDecimal;

public interface TopSellerProjection {
    Long getSellerId();
    String getSellerName();
    BigDecimal getTotalAmount();
}
