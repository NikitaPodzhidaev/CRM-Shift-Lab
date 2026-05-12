package shift.lab.crm.seller.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "sellers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SellerEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

}
