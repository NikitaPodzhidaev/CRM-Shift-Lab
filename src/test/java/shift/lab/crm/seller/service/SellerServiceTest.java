package shift.lab.crm.seller.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.db.SellerRepository;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.seller.mapper.SellerMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private SellerService sellerService;

    @Test
    void shouldCreateSeller() {
        String name = "Ivan";
        String contactInfo = "ivan@mail.ru";

        SellerEntity entityToSave = SellerEntity.builder()
                .id(null)
                .name(name)
                .contactInfo(contactInfo)
                .registrationDate(LocalDateTime.of(2026, 5, 12, 10, 0))
                .deleted(false)
                .build();

        SellerEntity savedEntity = SellerEntity.builder()
                .id(1L)
                .name(name)
                .contactInfo(contactInfo)
                .registrationDate(LocalDateTime.of(2026, 5, 12, 10, 0))
                .deleted(false)
                .build();

        Seller savedSeller = new Seller(
                1L,
                name,
                contactInfo,
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        when(sellerMapper.toEntity(any(Seller.class))).thenReturn(entityToSave);
        when(sellerRepository.save(entityToSave)).thenReturn(savedEntity);
        when(sellerMapper.toDomain(savedEntity)).thenReturn(savedSeller);

        Seller result = sellerService.createSeller(name, contactInfo);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(name, result.name());
        assertEquals(contactInfo, result.contactInfo());

        verify(sellerMapper).toEntity(any(Seller.class));
        verify(sellerRepository).save(entityToSave);
        verify(sellerMapper).toDomain(savedEntity);
    }

    @Test
    void shouldReturnSellerById() {
        Long sellerId = 1L;

        SellerEntity sellerEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 12, 10, 0))
                .deleted(false)
                .build();

        Seller seller = new Seller(
                sellerId,
                "Ivan",
                "ivan@mail.ru",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(sellerEntity));
        when(sellerMapper.toDomain(sellerEntity)).thenReturn(seller);

        Seller result = sellerService.getSellerById(sellerId);

        assertNotNull(result);
        assertEquals(sellerId, result.id());
        assertEquals("Ivan", result.name());

        verify(sellerRepository).findById(sellerId);
        verify(sellerMapper).toDomain(sellerEntity);
    }

    @Test
    void shouldThrowExceptionWhenSellerNotFoundById() {
        Long sellerId = 999L;

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                sellerService.getSellerById(sellerId)
        );

        verify(sellerRepository).findById(sellerId);
        verifyNoInteractions(sellerMapper);
    }

    @Test
    void shouldReturnAllSellers() {
        SellerEntity firstEntity = SellerEntity.builder()
                .id(1L)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 12, 10, 0))
                .deleted(false)
                .build();

        SellerEntity secondEntity = SellerEntity.builder()
                .id(2L)
                .name("Anna")
                .contactInfo("anna@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 13, 10, 0))
                .deleted(false)
                .build();

        Seller firstSeller = new Seller(
                1L,
                "Ivan",
                "ivan@mail.ru",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        Seller secondSeller = new Seller(
                2L,
                "Anna",
                "anna@mail.ru",
                LocalDateTime.of(2026, 5, 13, 10, 0)
        );

        when(sellerRepository.findAll()).thenReturn(List.of(firstEntity, secondEntity));
        when(sellerMapper.toDomain(firstEntity)).thenReturn(firstSeller);
        when(sellerMapper.toDomain(secondEntity)).thenReturn(secondSeller);

        List<Seller> result = sellerService.getAllSellers();

        assertEquals(2, result.size());
        assertEquals("Ivan", result.get(0).name());
        assertEquals("Anna", result.get(1).name());

        verify(sellerRepository).findAll();
        verify(sellerMapper).toDomain(firstEntity);
        verify(sellerMapper).toDomain(secondEntity);
    }

    @Test
    void shouldUpdateSeller() {
        Long sellerId = 1L;
        LocalDateTime registrationDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        SellerEntity existingEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Old Name")
                .contactInfo("old@mail.ru")
                .registrationDate(registrationDate)
                .deleted(false)
                .build();

        SellerEntity savedEntity = SellerEntity.builder()
                .id(sellerId)
                .name("New Name")
                .contactInfo("new@mail.ru")
                .registrationDate(registrationDate)
                .deleted(false)
                .build();

        Seller updatedSeller = new Seller(
                sellerId,
                "New Name",
                "new@mail.ru",
                registrationDate
        );

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(existingEntity));
        when(sellerRepository.save(any(SellerEntity.class))).thenReturn(savedEntity);
        when(sellerMapper.toDomain(savedEntity)).thenReturn(updatedSeller);

        Seller result = sellerService.updateSeller(sellerId, "New Name", "new@mail.ru");

        assertEquals(sellerId, result.id());
        assertEquals("New Name", result.name());
        assertEquals("new@mail.ru", result.contactInfo());
        assertEquals(registrationDate, result.registrationDate());

        verify(sellerRepository).findById(sellerId);
        verify(sellerRepository).save(any(SellerEntity.class));
        verify(sellerMapper).toDomain(savedEntity);
    }

    @Test
    void shouldDeleteSellerBySoftDelete() {
        Long sellerId = 1L;

        SellerEntity sellerEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 12, 10, 0))
                .deleted(false)
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(sellerEntity));

        sellerService.deleteSeller(sellerId);

        assertTrue(sellerEntity.isDeleted());

        verify(sellerRepository).findById(sellerId);
        verify(sellerRepository).save(sellerEntity);
        verify(sellerRepository, never()).deleteById(sellerId);
    }
}