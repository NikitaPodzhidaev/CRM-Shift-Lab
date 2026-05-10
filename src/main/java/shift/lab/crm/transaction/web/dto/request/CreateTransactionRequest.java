package shift.lab.crm.transaction.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequest(


        @NotBlank(message = "Seller id must not be blank")
        Long sellerId,

        @NotBlank(message = "Amount must not be blank")
        @Positive(message = "Amount must be more than 0")
        BigDecimal amount,

        @NotBlank(message = "Payment type must not be blank")
        String paymentType
) {
}
