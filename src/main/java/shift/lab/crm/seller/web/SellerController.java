package shift.lab.crm.seller.web;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.seller.service.SellerService;
import shift.lab.crm.seller.web.dto.request.CreateSellerRequest;
import shift.lab.crm.seller.web.dto.response.SellerResponse;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SellerResponse createSeller(@Valid @RequestBody CreateSellerRequest request){
        Seller newSeller = sellerService.createSeller(request.name(), request.contactInfo());
        return new SellerResponse(
                newSeller.id(),
                newSeller.name(),
                newSeller.contactInfo(),
                newSeller.registrationDate()
        );
    }

}
