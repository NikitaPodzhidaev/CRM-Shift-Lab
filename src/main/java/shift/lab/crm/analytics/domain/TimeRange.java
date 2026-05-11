package shift.lab.crm.analytics.domain;

import java.time.LocalDateTime;

public record TimeRange(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
