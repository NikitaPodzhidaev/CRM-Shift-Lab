package shift.lab.crm.analytics.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetSellerAmountLessThanRequest(

        @NotBlank(message = "Start time period must not be blank")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startPeriod,

        @NotBlank(message = "End time period must not be blank")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endPeriod,

        @NotBlank(message = "Compared-to amount must not be blank")
        @Positive(message = "Compared-to amount must be more than zero")
        BigDecimal comparedToAmount

) {
}
