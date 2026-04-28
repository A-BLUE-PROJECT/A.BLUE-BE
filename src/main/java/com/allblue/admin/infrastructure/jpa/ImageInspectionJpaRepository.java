package com.allblue.admin.infrastructure.jpa;

import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.model.InspectionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageInspectionJpaRepository extends JpaRepository<ImageInspection, Long> {
    Optional<ImageInspection> findByLookbookImageId(Long lookbookImageId);

    Page<ImageInspection> findByStatusIn(List<InspectionStatus> statuses, Pageable pageable);

    @Query(value = """
            SELECT ii.* FROM image_inspections ii
            INNER JOIN lookbook_images li ON ii.lookbook_image_id = li.id
            WHERE li.lookbook_id = :lookbookId AND ii.deleted = false
            """, nativeQuery = true)
    Optional<ImageInspection> findByLookbookId(@Param("lookbookId") Long lookbookId);
}
