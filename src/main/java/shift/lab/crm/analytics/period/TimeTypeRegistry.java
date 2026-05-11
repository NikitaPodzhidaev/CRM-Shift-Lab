package shift.lab.crm.analytics.period;

import org.springframework.stereotype.Component;
import shift.lab.crm.analytics.domain.TimeRange;
import shift.lab.crm.analytics.domain.TimeType;

import java.time.LocalDateTime;
import java.util.EnumMap;

@Component
public class TimeTypeRegistry {

    private final EnumMap<TimeType, TimeRangeStrategy> enumMap = new EnumMap<>(TimeType.class);


    public TimeTypeRegistry() {
        enumMap.put(TimeType.DAY, new DayTimeRangeStrategy());
        enumMap.put(TimeType.MONTH, new MonthTimeRangeStrategy());
        enumMap.put(TimeType.QUARTER, new QuarterTimeRangeStrategy());
        enumMap.put(TimeType.YEAR, new YearTimeRangeStrategy());
    }

    public TimeRange findTimeRange(TimeType timeType, LocalDateTime localDateTime){
        return enumMap.get(timeType).calculateTimeRange(localDateTime);
    }
}
