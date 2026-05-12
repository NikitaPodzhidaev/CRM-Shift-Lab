package shift.lab.crm.transaction.web;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shift.lab.crm.transaction.domain.Transaction;
import shift.lab.crm.transaction.mapper.TransactionDtoMapper;
import shift.lab.crm.transaction.service.TransactionService;
import shift.lab.crm.transaction.web.dto.request.CreateTransactionRequest;
import shift.lab.crm.transaction.web.dto.response.TransactionResponse;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionDtoMapper transactionDtoMapper;

    public TransactionController(TransactionService transactionService, TransactionDtoMapper transactionDtoMapper) {
        this.transactionService = transactionService;
        this.transactionDtoMapper = transactionDtoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody @Valid CreateTransactionRequest request){
        Transaction transaction = transactionService.createTransaction(

                request.sellerId(),
                request.amount(),
                request.paymentType()

        );
        return transactionDtoMapper.toResponse(transaction);
    }

    @GetMapping(params = "sellerId")
    public List<TransactionResponse> getTransactionsBySellerId(
            @RequestParam(required = false) Long sellerId
    ){
        return transactionService.getTransactionsBySellerId(sellerId).stream()
                .map(transactionDtoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable("id") Long id){
        return transactionDtoMapper.toResponse(transactionService.getTransactionById(id));
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions(){
        return transactionService.getAllTransactions()
                .stream()
                .map(transactionDtoMapper::toResponse)
                .toList();
    }


}
