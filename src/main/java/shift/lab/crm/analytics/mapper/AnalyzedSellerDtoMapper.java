package shift.lab.crm.analytics.mapper;

import org.springframework.stereotype.Component;
import shift.lab.crm.analytics.domain.SellerAmountLessThanProjection;
import shift.lab.crm.analytics.domain.TimeRange;
import shift.lab.crm.analytics.domain.TopSellerProjection;
import shift.lab.crm.analytics.web.dto.response.SellerLessThanResponse;
import shift.lab.crm.analytics.web.dto.response.TopSellerResponse;

@Component
public class AnalyzedSellerDtoMapper {

    public TopSellerResponse toTopSellerResponse(TopSellerProjection topSellerProjection, TimeRange timeRange){
        return new TopSellerResponse(
                topSellerProjection.getSellerId(),
                topSellerProjection.getSellerName(),
                topSellerProjection.getTotalAmount(),
                timeRange.startTime(),
                timeRange.endTime()
        );
    }

    public SellerLessThanResponse toSellerLessThanResponse(SellerAmountLessThanProjection projection, TimeRange timeRange){
        return new SellerLessThanResponse(
                projection.getSellerId(),
                projection.getSellerName(),
                projection.getTotalAmount(),
                projection.getComparedToAmount(),
                projection.getStartTime(),
                projection.getEndTime()
        );
    }

}
