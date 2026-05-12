package shift.lab.crm.analytics.period;

import org.junit.jupiter.api.Test;
import shift.lab.crm.analytics.domain.TimeRange;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeRangeStrategyTest {

    @Test
    void shouldCalculateDayRange() {
        DayTimeRangeStrategy strategy = new DayTimeRangeStrategy();

        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange range = strategy.calculateTimeRange(dateTime);

        assertEquals(
                LocalDateTime.of(2026, 5, 11, 0, 0),
                range.startTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 5, 12, 0, 0),
                range.endTime()
        );
    }

    @Test
    void shouldCalculateMonthRange() {
        MonthTimeRangeStrategy strategy = new MonthTimeRangeStrategy();

        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange range = strategy.calculateTimeRange(dateTime);

        assertEquals(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                range.startTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 6, 1, 0, 0),
                range.endTime()
        );
    }

    @Test
    void shouldCalculateQuarterRange() {
        QuarterTimeRangeStrategy strategy = new QuarterTimeRangeStrategy();

        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange range = strategy.calculateTimeRange(dateTime);

        assertEquals(
                LocalDateTime.of(2026, 4, 1, 0, 0),
                range.startTime()
        );
        assertEquals(
                LocalDateTime.of(2026, 7, 1, 0, 0),
                range.endTime()
        );
    }

    @Test
    void shouldCalculateYearRange() {
        YearTimeRangeStrategy strategy = new YearTimeRangeStrategy();

        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 15, 30, 45);

        TimeRange range = strategy.calculateTimeRange(dateTime);

        assertEquals(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                range.startTime()
        );
        assertEquals(
                LocalDateTime.of(2027, 1, 1, 0, 0),
                range.endTime()
        );
    }
}