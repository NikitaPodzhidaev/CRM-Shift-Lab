package shift.lab.crm.analytics.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shift.lab.crm.analytics.db.AnalyticsRepository;
import shift.lab.crm.analytics.domain.SellerAmountLessThanProjection;
import shift.lab.crm.analytics.domain.TimeRange;
import shift.lab.crm.analytics.domain.TimeType;
import shift.lab.crm.analytics.domain.TopSellerProjection;
import shift.lab.crm.analytics.exception.InvalidPeriodException;
import shift.lab.crm.analytics.mapper.AnalyzedSellerDtoMapper;
import shift.lab.crm.analytics.period.TimeTypeRegistry;
import shift.lab.crm.analytics.web.dto.response.SellerLessThanResponse;
import shift.lab.crm.analytics.web.dto.response.TopSellerResponse;
import shift.lab.crm.seller.db.SellerRepository;
import shift.lab.crm.transaction.db.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final TimeTypeRegistry timeTypeRegistry;
    private final AnalyzedSellerDtoMapper analyzedSellerDtoMapper;

    public AnalyticsService(TransactionRepository transactionRepository,
                            SellerRepository sellerRepository,
                            AnalyticsRepository analyticsRepository,
                            TimeTypeRegistry timeTypeRegistry, AnalyzedSellerDtoMapper analyzedSellerDtoMapper) {
        this.analyticsRepository = analyticsRepository;
        this.timeTypeRegistry = timeTypeRegistry;
        this.analyzedSellerDtoMapper = analyzedSellerDtoMapper;
    }


    public TopSellerResponse getTopSellerByPeriod(TimeType timeType, LocalDateTime localDateTime) {
        TimeRange timeRange = timeTypeRegistry.findTimeRange(timeType, localDateTime);

        List<TopSellerProjection> topSellerProjections = analyticsRepository.findTopSellerByPeriod(
                timeRange.startTime(),
                timeRange.endTime(),
                PageRequest.of(0, 1)
        );
        if (topSellerProjections.isEmpty()) {
            throw new EntityNotFoundException("No found top seller in this period: " +
                    timeRange.startTime() + " - " + timeRange.endTime());
        }
        return analyzedSellerDtoMapper.toTopSellerResponse(topSellerProjections.getFirst(), timeRange);
    }

    public List<SellerLessThanResponse> getSellersTotalAmountLessThanByPeriod(LocalDateTime startPeriod,
                                                                              LocalDateTime endPeriod,
                                                                              BigDecimal amount) {
        if(startPeriod.isAfter(endPeriod)){
            throw new InvalidPeriodException("Start period must be BEFORE endPeriod");
        }
        TimeRange timeRange = new TimeRange(startPeriod, endPeriod);
        List<SellerAmountLessThanProjection> sellersAmountLess =
                analyticsRepository.findSellersWithTotalAmountLessThanByPeriod(
                        startPeriod,
                        endPeriod,
                        amount
                );

        return sellersAmountLess.stream()
                .map(each ->
                        analyzedSellerDtoMapper.toSellerLessThanResponse(each, amount, timeRange)
                )
                .toList();

    }


}
