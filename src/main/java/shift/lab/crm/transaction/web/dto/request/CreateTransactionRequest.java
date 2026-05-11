package shift.lab.crm.transaction.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequest(


        @NotNull(message = "Seller id must not be null")
        Long sellerId,

        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be more than 0")
        BigDecimal amount,

        @NotBlank(message = "Payment type must not be blank")
        String paymentType
) {
}
