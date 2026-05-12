package shift.lab.crm.analytics.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import shift.lab.crm.analytics.db.AnalyticsRepository;
import shift.lab.crm.analytics.domain.SellerAmountLessThanProjection;
import shift.lab.crm.analytics.domain.TimeRange;
import shift.lab.crm.analytics.domain.TimeType;
import shift.lab.crm.analytics.domain.TopSellerProjection;
import shift.lab.crm.analytics.mapper.AnalyzedSellerDtoMapper;
import shift.lab.crm.analytics.period.TimeTypeRegistry;
import shift.lab.crm.analytics.web.dto.response.SellerLessThanResponse;
import shift.lab.crm.analytics.web.dto.response.TopSellerResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private TimeTypeRegistry timeTypeRegistry;

    @Mock
    private AnalyzedSellerDtoMapper analyzedSellerDtoMapper;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void shouldReturnTopSellerByPeriod() {
        TimeType timeType = TimeType.MONTH;
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 12, 0);
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 6, 1, 0, 0);

        TimeRange timeRange = new TimeRange(startTime, endTime);

        TopSellerProjection projection = mock(TopSellerProjection.class);

        TopSellerResponse expectedResponse = new TopSellerResponse(
                8L,
                "Ivan",
                new BigDecimal("15000.00"),
                startTime,
                endTime
        );

        when(timeTypeRegistry.findTimeRange(timeType, dateTime)).thenReturn(timeRange);
        when(analyticsRepository.findTopSellerByPeriod(
                startTime,
                endTime,
                PageRequest.of(0, 1)
        )).thenReturn(List.of(projection));
        when(analyzedSellerDtoMapper.toTopSellerResponse(projection, timeRange))
                .thenReturn(expectedResponse);

        TopSellerResponse result = analyticsService.getTopSellerByPeriod(timeType, dateTime);

        assertNotNull(result);
        assertEquals(8L, result.sellerId());
        assertEquals("Ivan", result.sellerName());
        assertEquals(new BigDecimal("15000.00"), result.totalAmount());
        assertEquals(startTime, result.periodStart());
        assertEquals(endTime, result.periodEnd());

        verify(timeTypeRegistry).findTimeRange(timeType, dateTime);
        verify(analyticsRepository).findTopSellerByPeriod(
                startTime,
                endTime,
                PageRequest.of(0, 1)
        );
        verify(analyzedSellerDtoMapper).toTopSellerResponse(projection, timeRange);
    }

    @Test
    void shouldThrowExceptionWhenTopSellerNotFoundByPeriod() {
        TimeType timeType = TimeType.MONTH;
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 12, 0);
        LocalDateTime startTime = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 6, 1, 0, 0);

        TimeRange timeRange = new TimeRange(startTime, endTime);

        when(timeTypeRegistry.findTimeRange(timeType, dateTime)).thenReturn(timeRange);
        when(analyticsRepository.findTopSellerByPeriod(
                startTime,
                endTime,
                PageRequest.of(0, 1)
        )).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->
                analyticsService.getTopSellerByPeriod(timeType, dateTime)
        );

        verify(timeTypeRegistry).findTimeRange(timeType, dateTime);
        verify(analyticsRepository).findTopSellerByPeriod(
                startTime,
                endTime,
                PageRequest.of(0, 1)
        );
        verifyNoInteractions(analyzedSellerDtoMapper);
    }

    @Test
    void shouldReturnSellersWithTotalAmountLessThanByPeriod() {
        LocalDateTime startPeriod = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2026, 3, 6, 0, 0);
        BigDecimal comparedToAmount = new BigDecimal("123000");

        SellerAmountLessThanProjection firstProjection = mock(SellerAmountLessThanProjection.class);
        SellerAmountLessThanProjection secondProjection = mock(SellerAmountLessThanProjection.class);

        SellerLessThanResponse firstResponse = new SellerLessThanResponse(
                16L,
                "Natalia",
                new BigDecimal("12500.00"),
                comparedToAmount,
                startPeriod,
                endPeriod
        );

        SellerLessThanResponse secondResponse = new SellerLessThanResponse(
                15L,
                "Alexey",
                new BigDecimal("800.00"),
                comparedToAmount,
                startPeriod,
                endPeriod
        );

        TimeRange expectedTimeRange = new TimeRange(startPeriod, endPeriod);

        when(analyticsRepository.findSellersWithTotalAmountLessThanByPeriod(
                startPeriod,
                endPeriod,
                comparedToAmount
        )).thenReturn(List.of(firstProjection, secondProjection));

        when(analyzedSellerDtoMapper.toSellerLessThanResponse(
                eq(firstProjection),
                eq(comparedToAmount),
                any(TimeRange.class)
        )).thenReturn(firstResponse);

        when(analyzedSellerDtoMapper.toSellerLessThanResponse(
                eq(secondProjection),
                eq(comparedToAmount),
                any(TimeRange.class)
        )).thenReturn(secondResponse);

        List<SellerLessThanResponse> result =
                analyticsService.getSellersTotalAmountLessThanByPeriod(
                        startPeriod,
                        endPeriod,
                        comparedToAmount
                );

        assertEquals(2, result.size());

        assertEquals(16L, result.get(0).sellerId());
        assertEquals("Natalia", result.get(0).sellerName());
        assertEquals(new BigDecimal("12500.00"), result.get(0).totalAmount());

        assertEquals(15L, result.get(1).sellerId());
        assertEquals("Alexey", result.get(1).sellerName());
        assertEquals(new BigDecimal("800.00"), result.get(1).totalAmount());

        verify(analyticsRepository).findSellersWithTotalAmountLessThanByPeriod(
                startPeriod,
                endPeriod,
                comparedToAmount
        );
        verify(analyzedSellerDtoMapper).toSellerLessThanResponse(
                eq(firstProjection),
                eq(comparedToAmount),
                any(TimeRange.class)
        );

        verify(analyzedSellerDtoMapper).toSellerLessThanResponse(
                eq(secondProjection),
                eq(comparedToAmount),
                any(TimeRange.class)
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoSellersWithTotalAmountLessThanByPeriod() {
        LocalDateTime startPeriod = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2026, 3, 6, 0, 0);
        BigDecimal comparedToAmount = new BigDecimal("1000");

        when(analyticsRepository.findSellersWithTotalAmountLessThanByPeriod(
                startPeriod,
                endPeriod,
                comparedToAmount
        )).thenReturn(List.of());

        List<SellerLessThanResponse> result =
                analyticsService.getSellersTotalAmountLessThanByPeriod(
                        startPeriod,
                        endPeriod,
                        comparedToAmount
                );

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(analyticsRepository).findSellersWithTotalAmountLessThanByPeriod(
                startPeriod,
                endPeriod,
                comparedToAmount
        );
        verifyNoInteractions(analyzedSellerDtoMapper);
    }
}