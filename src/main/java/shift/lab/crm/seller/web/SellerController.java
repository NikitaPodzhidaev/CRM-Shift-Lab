package shift.lab.crm.seller.web;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.seller.mapper.SellerDtoMapper;
import shift.lab.crm.seller.service.SellerService;
import shift.lab.crm.seller.web.dto.request.CreateSellerRequest;
import shift.lab.crm.seller.web.dto.request.UpdateSellerRequest;
import shift.lab.crm.seller.web.dto.response.SellerResponse;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final SellerDtoMapper sellerDtoMapper;

    public SellerController(SellerService sellerService, SellerDtoMapper sellerDtoMapper) {
        this.sellerService = sellerService;
        this.sellerDtoMapper = sellerDtoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
        public SellerResponse createSeller(@RequestBody @Valid CreateSellerRequest request){
            Seller newSeller = sellerService.createSeller(request.name(), request.contactInfo());
            return sellerDtoMapper.toResponse(newSeller);
        }

    @GetMapping("/{id}")
    public SellerResponse getSellerById(
            @PathVariable("id") Long id
    ){
        Seller searchedSeller = sellerService.getSellerById(id);
        return sellerDtoMapper.toResponse(searchedSeller);
    }

    @GetMapping
    public List<SellerResponse> getAllSellers(){
        return sellerService.getAllSellers().stream()
                .map(sellerDtoMapper::toResponse)
                .toList();

    }


    @PutMapping("/{id}")
    public SellerResponse updateSeller(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSellerRequest request
    ){
        Seller updatedSeller = sellerService.updateSeller(
                id,
                request.name(),
                request.contactInfo()
        );
        return sellerDtoMapper.toResponse(updatedSeller);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeller(
            @PathVariable Long id
    ){
        sellerService.deleteSeller(id);
    }

}
