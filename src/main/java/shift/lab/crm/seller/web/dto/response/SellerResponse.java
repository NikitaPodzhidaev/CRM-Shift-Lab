package shift.lab.crm.seller.web.dto.response;

import java.time.LocalDateTime;

public record SellerResponse(Long id,
                             String name,
                             String contactInfo,
                             LocalDateTime registrationDate) {
}
