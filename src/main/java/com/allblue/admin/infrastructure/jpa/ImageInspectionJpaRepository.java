package com.allblue.admin.infrastructure.jpa;

import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.model.InspectionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageInspectionJpaRepository extends JpaRepository<ImageInspection, Long> {
    Optional<ImageInspection> findByLookbookImageId(Long lookbookImageId);

    Page<ImageInspection> findByStatusIn(List<InspectionStatus> statuses, Pageable pageable);
}
