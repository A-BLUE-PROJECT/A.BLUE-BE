package com.allblue.lookbook.infrastructure.jpa;

import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LookbookJpaRepository extends JpaRepository<Lookbook, Long> {

    List<Lookbook> findByStatus(LookbookStatus status);

    List<Lookbook> findByStatusAndCreatedAtBefore(LookbookStatus status, LocalDateTime threshold);

    @Query("SELECT l FROM Lookbook l WHERE l.status = :status ORDER BY l.id DESC")
    List<Lookbook> findByStatusOrderByIdDesc(@Param("status") LookbookStatus status, Pageable pageable);

    @Query("SELECT l FROM Lookbook l WHERE l.status = :status AND l.id < :cursorId ORDER BY l.id DESC")
    List<Lookbook> findByStatusAndIdLessThanOrderByIdDesc(
            @Param("status") LookbookStatus status,
            @Param("cursorId") Long cursorId,
            Pageable pageable);
}
