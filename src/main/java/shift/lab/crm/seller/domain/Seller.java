package shift.lab.crm.seller.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Seller {

    private final Long id;
    private final String name;
    private final String contactInfo;
    private final LocalDateTime registrationDate;

    public Seller(String name, String contactInfo, LocalDateTime registrationDate){
        this(null, name, contactInfo, registrationDate);
    }

    public Seller(Long id, String name, String contactInfo, LocalDateTime registrationDate){
        validateName(name);
        validateContactInfo(contactInfo);
        validateRegistrationDate(registrationDate);
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
    }


    private void validateName(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Seller name must not be blank");
        }
    }

    private void validateContactInfo(String contactInfo){
        if(contactInfo == null || contactInfo.isBlank()){
            throw new IllegalArgumentException("Contact info must not be blank");
        }
    }

    private void validateRegistrationDate(LocalDateTime registrationDate){
        if(registrationDate == null){
            throw new IllegalArgumentException("Registration date must not be null");
        }
    }
}
