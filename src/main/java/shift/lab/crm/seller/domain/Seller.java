package shift.lab.crm.seller.domain;

import shift.lab.crm.common.exception.DomainValidationException;

import java.time.LocalDateTime;

public record Seller(Long id, String name, String contactInfo, LocalDateTime registrationDate) {

    public Seller(String name, String contactInfo, LocalDateTime registrationDate) {
        this(null, name, contactInfo, registrationDate);
    }

    public Seller {
        validateName(name);
        validateContactInfo(contactInfo);
        validateRegistrationDate(registrationDate);
    }


    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Seller name must not be blank");
        }
    }

    private void validateContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.isBlank()) {
            throw new DomainValidationException("Contact info must not be blank");
        }
    }

    private void validateRegistrationDate(LocalDateTime registrationDate) {
        if (registrationDate == null) {
            throw new DomainValidationException("Registration date must not be null");
        }
    }
}
