package shift.lab.crm.analytics.period;

import org.junit.jupiter.api.Test;
import shift.lab.crm.analytics.domain.TimeRange;
import shift.lab.crm.analytics.domain.TimeType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeTypeRegistryTest {

    private final TimeTypeRegistry timeTypeRegistry = new TimeTypeRegistry();

    @Test
    void shouldReturnDayRangeWhenTimeTypeIsDay() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange timeRange = timeTypeRegistry.findTimeRange(TimeType.DAY, dateTime);

        assertEquals(
                LocalDateTime.of(2026, 5, 11, 0, 0),
                timeRange.startTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 5, 12, 0, 0),
                timeRange.endTime()
        );
    }

    @Test
    void shouldReturnMonthRangeWhenTimeTypeIsMonth() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange timeRange = timeTypeRegistry.findTimeRange(TimeType.MONTH, dateTime);

        assertEquals(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                timeRange.startTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                timeRange.endTime()
        );
    }

    @Test
    void shouldReturnQuarterRangeWhenTimeTypeIsQuarter() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange timeRange = timeTypeRegistry.findTimeRange(TimeType.QUARTER, dateTime);

        assertEquals(
                LocalDateTime.of(2026, 4, 1, 0, 0),
                timeRange.startTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 7, 1, 0, 0),
                timeRange.endTime()
        );
    }

    @Test
    void shouldReturnYearRangeWhenTimeTypeIsYear() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange timeRange = timeTypeRegistry.findTimeRange(TimeType.YEAR, dateTime);

        assertEquals(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                timeRange.startTime()
        );
        assertEquals(
                LocalDateTime.of(2027, 1, 1, 0, 0),
                timeRange.endTime()
        );
    }
}