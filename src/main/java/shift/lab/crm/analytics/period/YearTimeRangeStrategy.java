package shift.lab.crm.analytics.period;

import shift.lab.crm.analytics.domain.TimeRange;

import java.time.LocalDateTime;

public class YearTimeRangeStrategy implements TimeRangeStrategy {

    @Override
    public TimeRange calculateTimeRange(LocalDateTime localDateTime) {
        LocalDateTime startYearTime = localDateTime.toLocalDate().withDayOfYear(1).atStartOfDay();
        LocalDateTime endYeartime = startYearTime.plusYears(1);
        return new TimeRange(startYearTime, endYeartime);
    }
}
