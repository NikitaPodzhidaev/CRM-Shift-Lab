package shift.lab.crm.analytics.domain;

import java.math.BigDecimal;

public interface SellerAmountLessThanProjection {
    Long getSellerId();
    String getSellerName();
    BigDecimal getTotalAmount();
}
