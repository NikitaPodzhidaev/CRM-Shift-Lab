package shift.lab.crm.analytics.period;

import shift.lab.crm.analytics.domain.TimeRange;

import java.time.LocalDateTime;

public interface TimeRangeStrategy {
    TimeRange calculateTimeRange(LocalDateTime localDateTime);
}
