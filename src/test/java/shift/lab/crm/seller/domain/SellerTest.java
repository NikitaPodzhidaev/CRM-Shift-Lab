package shift.lab.crm.seller.domain;

import org.junit.jupiter.api.Test;
import shift.lab.crm.common.exception.DomainValidationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SellerTest {

    @Test
    void shouldCreateSellerWithValidData() {
        Seller seller = new Seller(
                null,
                "Ivan",
                "ivan@mail.ru",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        assertEquals("Ivan", seller.name());
        assertEquals("ivan@mail.ru", seller.contactInfo());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(DomainValidationException.class, () ->
                new Seller(
                        null,
                        "",
                        "ivan@mail.ru",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenContactInfoIsBlank(){
        assertThrows(DomainValidationException.class, () ->
                new Seller(
                        null,
                        "Some Name",
                        "",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenRegistrationDateIsNull(){
        assertThrows(DomainValidationException.class, () ->
                new Seller(
                        null,
                        "Some Name",
                        "SomeName@example.com",
                        null
                )
        );
    }
}