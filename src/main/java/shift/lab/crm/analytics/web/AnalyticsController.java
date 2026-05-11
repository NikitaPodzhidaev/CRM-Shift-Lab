package shift.lab.crm.analytics.web;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.analytics.service.AnalyticsService;
import shift.lab.crm.analytics.web.dto.request.GetSellerAmountLessThanRequest;
import shift.lab.crm.analytics.web.dto.request.GetTopSellerByPeriodRequest;
import shift.lab.crm.analytics.web.dto.response.SellerLessThanResponse;
import shift.lab.crm.analytics.web.dto.response.TopSellerResponse;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;


    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/sellers/less-than")
    public List<SellerLessThanResponse> getAllSellerLessThan(
            @Valid @ModelAttribute GetSellerAmountLessThanRequest request
    ) {

        return analyticsService.getSellersTotalAmountLessThanByPeriod(
                request.startPeriod(),
                request.endPeriod(),
                request.comparedToAmount()
        );
    }

    @GetMapping("/top-seller")
    public TopSellerResponse getTopSellerByPeriod(
            @Valid @ModelAttribute GetTopSellerByPeriodRequest request
    ) {

        return analyticsService.getTopSellerByPeriod(
                request.timeType(),
                request.dateTime()
        );

    }


}
