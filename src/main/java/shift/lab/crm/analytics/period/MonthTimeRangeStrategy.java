package shift.lab.crm.analytics.period;

import shift.lab.crm.analytics.domain.TimeRange;

import java.time.LocalDateTime;

public class MonthTimeRangeStrategy implements TimeRangeStrategy {

    @Override
    public TimeRange calculateTimeRange(LocalDateTime localDateTime) {
        LocalDateTime startMonthTime = localDateTime.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endMonthTime = startMonthTime.plusMonths(1);
        return new TimeRange(startMonthTime, endMonthTime);
    }
}
