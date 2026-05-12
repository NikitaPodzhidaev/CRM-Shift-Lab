package shift.lab.crm.analytics.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shift.lab.crm.analytics.domain.TimeType;
import shift.lab.crm.analytics.service.AnalyticsService;
import shift.lab.crm.analytics.web.dto.response.SellerLessThanResponse;
import shift.lab.crm.analytics.web.dto.response.TopSellerResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalyticsService analyticsService;

    @Test
    void shouldReturnTopSellerByPeriod() throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2026, 5, 11, 0, 0);
        LocalDateTime periodStart = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime periodEnd = LocalDateTime.of(2026, 6, 1, 0, 0);

        TopSellerResponse response = new TopSellerResponse(
                8L,
                "Ivan",
                new BigDecimal("15000.00"),
                periodStart,
                periodEnd
        );

        when(analyticsService.getTopSellerByPeriod(TimeType.MONTH, dateTime))
                .thenReturn(response);

        mockMvc.perform(get("/analytics/top-seller")
                        .param("timeType", "MONTH")
                        .param("dateTime", "2026-05-11T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value(8))
                .andExpect(jsonPath("$.sellerName").value("Ivan"))
                .andExpect(jsonPath("$.totalAmount").value(15000.00));

        verify(analyticsService).getTopSellerByPeriod(TimeType.MONTH, dateTime);
    }

    @Test
    void shouldReturnBadRequestWhenTimeTypeIsInvalid() throws Exception {
        mockMvc.perform(get("/analytics/top-seller")
                        .param("timeType", "QUARTERПП")
                        .param("dateTime", "2026-05-11T00:00:00"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(analyticsService);
    }

    @Test
    void shouldReturnSellersWithTotalAmountLessThan() throws Exception {
        LocalDateTime startPeriod = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime endPeriod = LocalDateTime.of(2026, 6, 1, 0, 0);
        BigDecimal comparedToAmount = new BigDecimal("10000");

        SellerLessThanResponse firstResponse = new SellerLessThanResponse(
                8L,
                "Ivan",
                new BigDecimal("3000.00"),
                comparedToAmount,
                startPeriod,
                endPeriod
        );

        SellerLessThanResponse secondResponse = new SellerLessThanResponse(
                9L,
                "Anna",
                BigDecimal.ZERO,
                comparedToAmount,
                startPeriod,
                endPeriod
        );

        when(analyticsService.getSellersTotalAmountLessThanByPeriod(
                startPeriod,
                endPeriod,
                comparedToAmount
        )).thenReturn(List.of(firstResponse, secondResponse));

        mockMvc.perform(get("/analytics/sellers/less-than")
                        .param("startPeriod", "2026-05-01T00:00:00")
                        .param("endPeriod", "2026-06-01T00:00:00")
                        .param("comparedToAmount", "10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sellerId").value(8))
                .andExpect(jsonPath("$[0].sellerName").value("Ivan"))
                .andExpect(jsonPath("$[0].totalAmount").value(3000.00))
                .andExpect(jsonPath("$[1].sellerId").value(9))
                .andExpect(jsonPath("$[1].sellerName").value("Anna"))
                .andExpect(jsonPath("$[1].totalAmount").value(0));

        verify(analyticsService).getSellersTotalAmountLessThanByPeriod(
                startPeriod,
                endPeriod,
                comparedToAmount
        );
    }

    @Test
    void shouldReturnBadRequestWhenComparedToAmountIsNegative() throws Exception {
        mockMvc.perform(get("/analytics/sellers/less-than")
                        .param("startPeriod", "2026-05-01T00:00:00")
                        .param("endPeriod", "2026-06-01T00:00:00")
                        .param("comparedToAmount", "-100"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(analyticsService);
    }

    @Test
    void shouldReturnBadRequestWhenStartPeriodIsMissing() throws Exception {
        mockMvc.perform(get("/analytics/sellers/less-than")
                        .param("endPeriod", "2026-06-01T00:00:00")
                        .param("comparedToAmount", "10000"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(analyticsService);
    }
}