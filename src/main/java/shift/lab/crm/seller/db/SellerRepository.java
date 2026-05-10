package shift.lab.crm.seller.db;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.lab.crm.common.analytics.TimeType;

import java.time.LocalDateTime;

public interface SellerRepository extends JpaRepository<SellerEntity, Long> {


    public SellerEntity findMostValuableSellerByTime(TimeType timeType, LocalDateTime localDateTime);



}
