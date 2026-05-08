package shift.lab.crm.seller.mapper;

import org.springframework.stereotype.Component;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.seller.web.dto.response.SellerResponse;

@Component
public class SellerDtoMapper {

    public SellerResponse toResponse(Seller seller){
        return new SellerResponse(seller.id(),
                seller.name(),
                seller.contactInfo(),
                seller.registrationDate());
    }

}
