package shift.lab.crm.transaction.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.db.SellerRepository;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.transaction.db.TransactionEntity;
import shift.lab.crm.transaction.db.TransactionRepository;
import shift.lab.crm.transaction.domain.Transaction;
import shift.lab.crm.transaction.mapper.TransactionMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final TransactionMapper transactionMapper;


    public TransactionService(TransactionRepository transactionRepository, SellerRepository sellerRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.sellerRepository = sellerRepository;
        this.transactionMapper = transactionMapper;
    }

    public Transaction createTransaction(Long sellerId,
                                         BigDecimal amount,
                                         String paymentType){
        LocalDateTime transactionDate = LocalDateTime.now();

        SellerEntity sellerEntity = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Not found seller by id = " + sellerId));
        Transaction transaction = new Transaction(sellerId, amount, paymentType, transactionDate);
        TransactionEntity transactionEntity = transactionMapper.toEntity(transaction, sellerEntity);
        TransactionEntity savedTransactionEntity = transactionRepository.save(transactionEntity);
        return transactionMapper.toDomain(savedTransactionEntity);

    }

    public Transaction getTransactionById(Long id){
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found transaction by id = " + id));
        return transactionMapper.toDomain(transactionEntity);
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDomain)
                .toList();
    }

    public List<Transaction> getTransactionsBySellerId(Long sellerId){
        if(!sellerRepository.existsById(sellerId)){
            throw new EntityNotFoundException("Not found seller by id = " + sellerId);
        }
        return transactionRepository.findBySeller_Id(sellerId)
                .stream()
                .map(transactionMapper::toDomain)
                .toList();
    }


}
