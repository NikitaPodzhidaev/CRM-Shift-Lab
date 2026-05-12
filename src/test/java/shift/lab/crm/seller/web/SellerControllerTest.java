package shift.lab.crm.seller.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.seller.mapper.SellerDtoMapper;
import shift.lab.crm.seller.service.SellerService;
import shift.lab.crm.seller.web.dto.response.SellerResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerController.class)
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private SellerService sellerService;

    @MockitoBean
    private SellerDtoMapper sellerDtoMapper;

    @Test
    void shouldCreateSeller() throws Exception {
        LocalDateTime registrationDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        Seller seller = new Seller(
                1L,
                "Ivan",
                "ivan@mail.ru",
                registrationDate
        );

        SellerResponse response = new SellerResponse(
                1L,
                "Ivan",
                "ivan@mail.ru",
                registrationDate
        );

        when(sellerService.createSeller("Ivan", "ivan@mail.ru"))
                .thenReturn(seller);
        when(sellerDtoMapper.toResponse(seller))
                .thenReturn(response);

        String requestBody = """
                {
                  "name": "Ivan",
                  "contactInfo": "ivan@mail.ru"
                }
                """;

        mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.contactInfo").value("ivan@mail.ru"));

        verify(sellerService).createSeller("Ivan", "ivan@mail.ru");
        verify(sellerDtoMapper).toResponse(seller);
    }

    @Test
    void shouldReturnSellerById() throws Exception {
        LocalDateTime registrationDate = LocalDateTime.of(2026, 5, 12, 10, 0);

        Seller seller = new Seller(
                1L,
                "Ivan",
                "ivan@mail.ru",
                registrationDate
        );

        SellerResponse response = new SellerResponse(
                1L,
                "Ivan",
                "ivan@mail.ru",
                registrationDate
        );

        when(sellerService.getSellerById(1L)).thenReturn(seller);
        when(sellerDtoMapper.toResponse(seller)).thenReturn(response);

        mockMvc.perform(get("/sellers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.contactInfo").value("ivan@mail.ru"));

        verify(sellerService).getSellerById(1L);
        verify(sellerDtoMapper).toResponse(seller);
    }

    @Test
    void shouldReturnAllSellers() throws Exception {
        LocalDateTime firstDate = LocalDateTime.of(2026, 5, 12, 10, 0);
        LocalDateTime secondDate = LocalDateTime.of(2026, 5, 13, 10, 0);

        Seller firstSeller = new Seller(
                1L,
                "Ivan",
                "ivan@mail.ru",
                firstDate
        );

        Seller secondSeller = new Seller(
                2L,
                "Anna",
                "anna@mail.ru",
                secondDate
        );

        SellerResponse firstResponse = new SellerResponse(
                1L,
                "Ivan",
                "ivan@mail.ru",
                firstDate
        );

        SellerResponse secondResponse = new SellerResponse(
                2L,
                "Anna",
                "anna@mail.ru",
                secondDate
        );

        when(sellerService.getAllSellers())
                .thenReturn(List.of(firstSeller, secondSeller));
        when(sellerDtoMapper.toResponse(firstSeller))
                .thenReturn(firstResponse);
        when(sellerDtoMapper.toResponse(secondSeller))
                .thenReturn(secondResponse);

        mockMvc.perform(get("/sellers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Anna"));

        verify(sellerService).getAllSellers();
        verify(sellerDtoMapper).toResponse(firstSeller);
        verify(sellerDtoMapper).toResponse(secondSeller);
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "contactInfo": "ivan@mail.ru"
                }
                """;

        mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sellerService);
        verifyNoInteractions(sellerDtoMapper);
    }

    @Test
    void shouldDeleteSeller() throws Exception {
        mockMvc.perform(delete("/sellers/1"))
                .andExpect(status().isNoContent());

        verify(sellerService).deleteSeller(1L);
    }
}