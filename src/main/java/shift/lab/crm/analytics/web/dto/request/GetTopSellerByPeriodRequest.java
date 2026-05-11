package shift.lab.crm.analytics.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import shift.lab.crm.analytics.domain.TimeType;

import java.time.LocalDateTime;

public record GetTopSellerByPeriodRequest(


        @NotBlank(message = "Time type must not be blank")
        TimeType timeType,

        @NotBlank(message = "DateTime must not be blank")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateTime

) {
}
