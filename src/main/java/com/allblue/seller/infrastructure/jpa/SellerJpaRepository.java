package com.allblue.seller.infrastructure.jpa;

import com.allblue.seller.domain.model.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerJpaRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByMallId(String mallId);
    boolean existsByMallId(String mallId);
}
