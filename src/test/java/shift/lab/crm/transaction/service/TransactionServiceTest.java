package shift.lab.crm.transaction.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.db.SellerRepository;
import shift.lab.crm.transaction.db.TransactionEntity;
import shift.lab.crm.transaction.db.TransactionRepository;
import shift.lab.crm.transaction.domain.Transaction;
import shift.lab.crm.transaction.exception.UnknownPaymentTypeException;
import shift.lab.crm.transaction.mapper.TransactionMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateTransactionWhenSellerExistsAndNotDeleted() {
        Long sellerId = 8L;
        BigDecimal amount = new BigDecimal("1500.00");
        String paymentType = "CARD";
        LocalDateTime transactionDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        SellerEntity sellerEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 1, 10, 0))
                .deleted(false)
                .build();

        TransactionEntity entityToSave = new TransactionEntity(
                null,
                sellerEntity,
                amount,
                paymentType,
                transactionDate
        );

        TransactionEntity savedEntity = new TransactionEntity(
                1L,
                sellerEntity,
                amount,
                paymentType,
                transactionDate
        );

        Transaction savedTransaction = new Transaction(
                1L,
                sellerId,
                amount,
                paymentType,
                transactionDate
        );

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(sellerEntity));
        when(transactionMapper.toEntity(any(Transaction.class), any(SellerEntity.class))).thenReturn(entityToSave);
        when(transactionRepository.save(entityToSave)).thenReturn(savedEntity);
        when(transactionMapper.toDomain(savedEntity)).thenReturn(savedTransaction);

        Transaction result = transactionService.createTransaction(sellerId, amount, paymentType);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(sellerId, result.sellerId());
        assertEquals(amount, result.amount());
        assertEquals(paymentType, result.paymentType());

        verify(sellerRepository).findById(sellerId);
        verify(transactionMapper).toEntity(any(Transaction.class), eq(sellerEntity));
        verify(transactionRepository).save(entityToSave);
        verify(transactionMapper).toDomain(savedEntity);
    }

    @Test
    void shouldThrowExceptionWhenSellerNotFoundOnCreateTransaction() {
        Long sellerId = 999L;

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                transactionService.createTransaction(
                        sellerId,
                        new BigDecimal("1500.00"),
                        "CARD"
                )
        );

        verify(sellerRepository).findById(sellerId);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void shouldThrowExceptionWhenSellerIsDeletedOnCreateTransaction() {
        Long sellerId = 8L;

        SellerEntity deletedSeller = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 1, 10, 0))
                .deleted(true)
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(deletedSeller));

        assertThrows(EntityNotFoundException.class, () ->
                transactionService.createTransaction(
                        sellerId,
                        new BigDecimal("1500.00"),
                        "CARD"
                )
        );

        verify(sellerRepository).findById(sellerId);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void shouldThrowExceptionWhenPaymentTypeIsUnknown() {

        assertThrows(UnknownPaymentTypeException.class, () ->
                transactionService.createTransaction(
                        8L,
                        new BigDecimal("1500.00"),
                        "BITCOIN"
                )
        );

        verifyNoInteractions(sellerRepository);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void shouldReturnTransactionById() {
        Long transactionId = 1L;
        Long sellerId = 8L;
        BigDecimal amount = new BigDecimal("1500.00");
        String paymentType = "CARD";
        LocalDateTime transactionDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        SellerEntity sellerEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 1, 10, 0))
                .deleted(false)
                .build();

        TransactionEntity transactionEntity = new TransactionEntity(
                transactionId,
                sellerEntity,
                amount,
                paymentType,
                transactionDate
        );

        Transaction transaction = new Transaction(
                transactionId,
                sellerId,
                amount,
                paymentType,
                transactionDate
        );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionEntity));
        when(transactionMapper.toDomain(transactionEntity)).thenReturn(transaction);

        Transaction result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.id());
        assertEquals(sellerId, result.sellerId());
        assertEquals(amount, result.amount());

        verify(transactionRepository).findById(transactionId);
        verify(transactionMapper).toDomain(transactionEntity);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFoundById() {
        Long transactionId = 999L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                transactionService.getTransactionById(transactionId)
        );

        verify(transactionRepository).findById(transactionId);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void shouldReturnAllTransactions() {
        Long sellerId = 8L;

        SellerEntity sellerEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 1, 10, 0))
                .deleted(false)
                .build();

        TransactionEntity firstEntity = new TransactionEntity(
                1L,
                sellerEntity,
                new BigDecimal("1500.00"),
                "CARD",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        TransactionEntity secondEntity = new TransactionEntity(
                2L,
                sellerEntity,
                new BigDecimal("2300.00"),
                "CASH",
                LocalDateTime.of(2026, 5, 13, 11, 0)
        );

        Transaction firstTransaction = new Transaction(
                1L,
                sellerId,
                new BigDecimal("1500.00"),
                "CARD",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        Transaction secondTransaction = new Transaction(
                2L,
                sellerId,
                new BigDecimal("2300.00"),
                "CASH",
                LocalDateTime.of(2026, 5, 13, 11, 0)
        );

        when(transactionRepository.findAllWithSeller()).thenReturn(List.of(firstEntity, secondEntity));
        when(transactionMapper.toDomain(firstEntity)).thenReturn(firstTransaction);
        when(transactionMapper.toDomain(secondEntity)).thenReturn(secondTransaction);

        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(transactionRepository).findAllWithSeller();
        verify(transactionMapper).toDomain(firstEntity);
        verify(transactionMapper).toDomain(secondEntity);
    }

    @Test
    void shouldReturnTransactionsBySellerId() {
        Long sellerId = 8L;

        SellerEntity sellerEntity = SellerEntity.builder()
                .id(sellerId)
                .name("Ivan")
                .contactInfo("ivan@mail.ru")
                .registrationDate(LocalDateTime.of(2026, 5, 1, 10, 0))
                .deleted(false)
                .build();

        TransactionEntity transactionEntity = new TransactionEntity(
                1L,
                sellerEntity,
                new BigDecimal("1500.00"),
                "CARD",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        Transaction transaction = new Transaction(
                1L,
                sellerId,
                new BigDecimal("1500.00"),
                "CARD",
                LocalDateTime.of(2026, 5, 12, 10, 0)
        );

        when(sellerRepository.existsById(sellerId)).thenReturn(true);
        when(transactionRepository.findBySellerIdWithSeller(sellerId)).thenReturn(List.of(transactionEntity));
        when(transactionMapper.toDomain(transactionEntity)).thenReturn(transaction);

        List<Transaction> result = transactionService.getTransactionsBySellerId(sellerId);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(sellerId, result.get(0).sellerId());

        verify(sellerRepository).existsById(sellerId);
        verify(transactionRepository).findBySellerIdWithSeller(sellerId);
        verify(transactionMapper).toDomain(transactionEntity);
    }

    @Test
    void shouldThrowExceptionWhenSellerNotFoundOnGetTransactionsBySellerId() {
        Long sellerId = 999L;

        when(sellerRepository.existsById(sellerId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->
                transactionService.getTransactionsBySellerId(sellerId)
        );

        verify(sellerRepository).existsById(sellerId);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }
}