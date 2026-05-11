package shift.lab.crm.analytics.period;

import shift.lab.crm.analytics.domain.TimeRange;

import java.time.LocalDateTime;

public class DayTimeRangeStrategy implements TimeRangeStrategy {

    @Override
    public TimeRange calculateTimeRange(LocalDateTime localDateTime) {
        LocalDateTime startDayTime = localDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDayTime = startDayTime.plusDays(1);
        return new TimeRange(startDayTime, endDayTime);
    }

}
