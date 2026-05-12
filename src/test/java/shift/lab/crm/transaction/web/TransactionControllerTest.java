package shift.lab.crm.transaction.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shift.lab.crm.transaction.domain.Transaction;
import shift.lab.crm.transaction.mapper.TransactionDtoMapper;
import shift.lab.crm.transaction.service.TransactionService;
import shift.lab.crm.transaction.web.dto.response.TransactionResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private TransactionDtoMapper transactionDtoMapper;

    @Test
    void shouldCreateTransaction() throws Exception {
        LocalDateTime transactionDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        Transaction transaction = new Transaction(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                transactionDate
        );

        TransactionResponse response = new TransactionResponse(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                transactionDate
        );

        when(transactionService.createTransaction(
                8L,
                new BigDecimal("1500.00"),
                "CARD"
        )).thenReturn(transaction);

        when(transactionDtoMapper.toResponse(transaction)).thenReturn(response);

        String requestBody = """
                {
                  "sellerId": 8,
                  "amount": 1500.00,
                  "paymentType": "CARD"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sellerId").value(8))
                .andExpect(jsonPath("$.amount").value(1500.00))
                .andExpect(jsonPath("$.paymentType").value("CARD"));

        verify(transactionService).createTransaction(
                8L,
                new BigDecimal("1500.00"),
                "CARD"
        );
        verify(transactionDtoMapper).toResponse(transaction);
    }

    @Test
    void shouldReturnTransactionById() throws Exception {
        LocalDateTime transactionDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        Transaction transaction = new Transaction(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                transactionDate
        );

        TransactionResponse response = new TransactionResponse(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                transactionDate
        );

        when(transactionService.getTransactionById(1L)).thenReturn(transaction);
        when(transactionDtoMapper.toResponse(transaction)).thenReturn(response);

        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sellerId").value(8))
                .andExpect(jsonPath("$.paymentType").value("CARD"));

        verify(transactionService).getTransactionById(1L);
        verify(transactionDtoMapper).toResponse(transaction);
    }

    @Test
    void shouldReturnAllTransactions() throws Exception {
        LocalDateTime firstDate = LocalDateTime.of(2026, 5, 12, 10, 0);
        LocalDateTime secondDate = LocalDateTime.of(2026, 5, 13, 11, 0);

        Transaction firstTransaction = new Transaction(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                firstDate
        );

        Transaction secondTransaction = new Transaction(
                2L,
                9L,
                new BigDecimal("2300.00"),
                "CASH",
                secondDate
        );

        TransactionResponse firstResponse = new TransactionResponse(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                firstDate
        );

        TransactionResponse secondResponse = new TransactionResponse(
                2L,
                9L,
                new BigDecimal("2300.00"),
                "CASH",
                secondDate
        );

        when(transactionService.getAllTransactions())
                .thenReturn(List.of(firstTransaction, secondTransaction));
        when(transactionDtoMapper.toResponse(firstTransaction))
                .thenReturn(firstResponse);
        when(transactionDtoMapper.toResponse(secondTransaction))
                .thenReturn(secondResponse);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(8))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].sellerId").value(9));

        verify(transactionService).getAllTransactions();
        verify(transactionDtoMapper).toResponse(firstTransaction);
        verify(transactionDtoMapper).toResponse(secondTransaction);
    }

    @Test
    void shouldReturnTransactionsBySellerId() throws Exception {
        LocalDateTime transactionDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        Transaction transaction = new Transaction(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                transactionDate
        );

        TransactionResponse response = new TransactionResponse(
                1L,
                8L,
                new BigDecimal("1500.00"),
                "CARD",
                transactionDate
        );

        when(transactionService.getTransactionsBySellerId(8L))
                .thenReturn(List.of(transaction));
        when(transactionDtoMapper.toResponse(transaction))
                .thenReturn(response);

        mockMvc.perform(get("/transactions?sellerId=8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(8))
                .andExpect(jsonPath("$[0].paymentType").value("CARD"));

        verify(transactionService).getTransactionsBySellerId(8L);
        verify(transactionDtoMapper).toResponse(transaction);
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsNegative() throws Exception {
        String requestBody = """
                {
                  "sellerId": 8,
                  "amount": -100.00,
                  "paymentType": "CARD"
                }
                """;

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(transactionService);
        verifyNoInteractions(transactionDtoMapper);
    }
}