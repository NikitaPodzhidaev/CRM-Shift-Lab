package shift.lab.crm.seller.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSellerRequest(

        @NotBlank(message = "Name mustn't be empty")
        String name,
        @Size(max = 1000, message = "Description mustn't be > 1000 characters")
        String contactInfo
) {
}
