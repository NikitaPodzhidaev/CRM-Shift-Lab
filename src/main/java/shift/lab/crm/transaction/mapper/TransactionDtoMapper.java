package shift.lab.crm.transaction.mapper;

import org.springframework.stereotype.Component;
import shift.lab.crm.transaction.domain.Transaction;
import shift.lab.crm.transaction.web.dto.response.TransactionResponse;

@Component
public class TransactionDtoMapper {

    public TransactionResponse toResponse(Transaction transaction){
        return new TransactionResponse(
                transaction.getId(),
                transaction.getSeller(),
                transaction.getAmount(),
                transaction.getPaymentType(),
                transaction.getTransactionDate()
        );
    }

}
