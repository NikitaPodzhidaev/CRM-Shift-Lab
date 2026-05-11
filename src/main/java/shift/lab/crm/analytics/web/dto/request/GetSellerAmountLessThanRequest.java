package shift.lab.crm.analytics.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetSellerAmountLessThanRequest(

        @NotNull(message = "Start time period must not be null")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startPeriod,

        @NotNull(message = "End time period must not be null")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endPeriod,

        @NotNull(message = "Compared-to amount must not be null")
        @Positive(message = "Compared-to amount must be more than zero")
        BigDecimal comparedToAmount

) {
}
