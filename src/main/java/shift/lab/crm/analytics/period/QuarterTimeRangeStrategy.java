package shift.lab.crm.analytics.period;

import shift.lab.crm.analytics.domain.TimeRange;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;

public class QuarterTimeRangeStrategy implements TimeRangeStrategy {

    @Override
    public TimeRange calculateTimeRange(LocalDateTime dateTime) {
        LocalDateTime startPeriod = dateTime
                .with(IsoFields.DAY_OF_QUARTER, 1)
                .toLocalDate()
                .atStartOfDay();

        LocalDateTime endPeriod = startPeriod.plus(1, IsoFields.QUARTER_YEARS);

        return new TimeRange(startPeriod, endPeriod);
    }
}
