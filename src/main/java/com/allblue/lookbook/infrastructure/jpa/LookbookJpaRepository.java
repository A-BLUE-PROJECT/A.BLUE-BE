package com.allblue.lookbook.infrastructure.jpa;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LookbookJpaRepository extends JpaRepository<Lookbook, Long> {
    List<Lookbook> findByStatusAndCreatedAtBefore(LookbookStatus status, LocalDateTime threshold);
}
