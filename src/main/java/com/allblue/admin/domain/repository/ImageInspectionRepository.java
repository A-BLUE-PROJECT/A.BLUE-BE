package com.allblue.admin.domain.repository;

import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.model.InspectionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImageInspectionRepository {
    ImageInspection getById(Long id);

    ImageInspection getByLookbookImageId(Long lookbookImageId);

    Optional<ImageInspection> findByLookbookImageId(Long lookbookImageId);

    Page<ImageInspection> getByStatuses(List<InspectionStatus> statuses, Pageable pageable);

    ImageInspection save(ImageInspection imageInspection);
}
