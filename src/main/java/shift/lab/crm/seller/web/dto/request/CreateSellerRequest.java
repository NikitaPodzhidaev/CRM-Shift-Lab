package shift.lab.crm.seller.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSellerRequest(

        @NotBlank(message = "Name must not be empty")
        @Size(max = 255, message = "Name must not be > 255 characters")
        String name,

        @Size(max = 1000, message = "Description must not be > 1000 characters")
        @NotBlank(message = "Contact info must not be empty")
        String contactInfo) {
}
