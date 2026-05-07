package shift.lab.crm.seller.mapper;

import org.springframework.stereotype.Component;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.domain.Seller;

@Component
public class SellerMapper {

    public SellerEntity toEntity(Seller seller) {
        return SellerEntity.builder()
                .id(seller.id())
                .name(seller.name())
                .contactInfo(seller.contactInfo())
                .registrationDate(seller.registrationDate())
                .build();
    }

    public Seller toDomain(SellerEntity sellerEntity) {
        return new Seller(
                sellerEntity.getId(),
                sellerEntity.getName(),
                sellerEntity.getContactInfo(),
                sellerEntity.getRegistrationDate()
        );
    }

}
