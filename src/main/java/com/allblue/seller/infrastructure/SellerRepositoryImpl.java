package com.allblue.seller.infrastructure;

import com.allblue.seller.domain.model.Seller;
import com.allblue.seller.domain.repository.SellerRepository;
import com.allblue.seller.infrastructure.jpa.SellerJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerRepositoryImpl implements SellerRepository {

    private final SellerJpaRepository sellerJpaRepository;

    @Override
    public Seller save(Seller seller) {
        return sellerJpaRepository.save(seller);
    }

    @Override
    public Optional<Seller> findByMallId(String mallId) {
        return sellerJpaRepository.findByMallId(mallId);
    }

    @Override
    public boolean existsByMallId(String mallId) {
        return sellerJpaRepository.existsByMallId(mallId);
    }
}
