package com.allblue.seller.domain.repository;

import com.allblue.seller.domain.model.Seller;
import java.util.Optional;

public interface SellerRepository {
    Seller save(Seller seller);
    Optional<Seller> findByMallId(String mallId);
    boolean existsByMallId(String mallId);
}
