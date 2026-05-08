package shift.lab.crm.seller.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import shift.lab.crm.seller.db.SellerEntity;
import shift.lab.crm.seller.db.SellerRepository;
import shift.lab.crm.seller.domain.Seller;
import shift.lab.crm.seller.mapper.SellerMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    public SellerService(SellerRepository sellerRepository, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
    }

    public Seller createSeller(String name, String contactInfo){
        LocalDateTime registrationDate = LocalDateTime.now();
        Seller newSeller = new Seller(name, contactInfo, registrationDate);
        SellerEntity newSellerEntity = sellerMapper.toEntity(newSeller);
        SellerEntity savedSellerEntity = sellerRepository.save(newSellerEntity);
        return sellerMapper.toDomain(savedSellerEntity);
    }

    public Seller getSellerById(Long id){
        SellerEntity searchedEntity = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found seller by id = " + id));
        return sellerMapper.toDomain(searchedEntity);
    }

    public List<Seller> getAllSellers(){
        List<SellerEntity> allSearchedSellers = sellerRepository.findAll();
        return allSearchedSellers.stream()
                .map(sellerMapper::toDomain)
                .toList();
    }

    public Seller updateSeller(Long id, String name, String contactInfo){
        SellerEntity existingEntity = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found seller by id = " + id));

        SellerEntity updatedEntity = SellerEntity.builder()
                .id(existingEntity.getId())
                .name(name)
                .contactInfo(contactInfo)
                .registrationDate(existingEntity.getRegistrationDate())
                .build();

        SellerEntity savedEntity = sellerRepository.save(updatedEntity);

        return sellerMapper.toDomain(savedEntity);
    }

    public void deleteSeller(Long id){
        SellerEntity existingEntity = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found seller by id = " + id));
        sellerRepository.delete(existingEntity);
    }

}
