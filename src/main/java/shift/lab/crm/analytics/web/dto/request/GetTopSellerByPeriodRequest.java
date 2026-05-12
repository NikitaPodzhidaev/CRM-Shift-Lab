package shift.lab.crm.analytics.web.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import shift.lab.crm.analytics.domain.TimeType;

import java.time.LocalDateTime;

public record GetTopSellerByPeriodRequest(


        @NotNull(message = "Time type must not be null")
        TimeType timeType,

        @NotNull(message = "DateTime must not be null")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateTime

) {
}
