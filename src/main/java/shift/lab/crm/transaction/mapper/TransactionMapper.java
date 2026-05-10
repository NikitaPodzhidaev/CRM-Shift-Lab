package shift.lab.crm.transaction.mapper;


import org.springframework.stereotype.Component;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.mapper.SellerMapper;
import shift.lab.crm.transaction.db.TransactionEntity;
import shift.lab.crm.transaction.domain.Transaction;

@Component
public class TransactionMapper {

    private final SellerMapper sellerMapper;

    public TransactionMapper(SellerMapper sellerMapper) {
        this.sellerMapper = sellerMapper;
    }

    public TransactionEntity toEntity(Transaction transaction, SellerEntity sellerEntity) {
        return new TransactionEntity(
                transaction.getId(),
                sellerEntity,
                transaction.getAmount(),
                transaction.getPaymentType(),
                transaction.getTransactionDate()
        );
    }

    public Transaction toDomain(TransactionEntity transactionEntity){
        return new Transaction(
                transactionEntity.getId(),
                transactionEntity.getSeller().getId(),
                transactionEntity.getAmount(),
                transactionEntity.getPaymentType(),
                transactionEntity.getTransactionDate()

        );
    }

}
