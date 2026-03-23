package com.allblue.admin.infrastructure;

import com.allblue.admin.domain.exception.AdminBusinessException;
import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.model.InspectionStatus;
import com.allblue.admin.domain.repository.ImageInspectionRepository;
import com.allblue.admin.infrastructure.jpa.ImageInspectionJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageInspectionRepositoryImpl implements ImageInspectionRepository {

    private final ImageInspectionJpaRepository imageInspectionJpaRepository;

    @Override
    public ImageInspection getById(Long id) {
        return imageInspectionJpaRepository
                .findById(id)
                .orElseThrow(() -> new AdminBusinessException(AdminErrorCode.INSPECTION_NOT_FOUND));
    }

    @Override
    public ImageInspection getByLookbookImageId(Long lookbookImageId) {
        return imageInspectionJpaRepository
                .findByLookbookImageId(lookbookImageId)
                .orElseThrow(() -> new AdminBusinessException(AdminErrorCode.INSPECTION_NOT_FOUND));
    }

    @Override
    public Optional<ImageInspection> findByLookbookImageId(Long lookbookImageId) {
        return imageInspectionJpaRepository.findByLookbookImageId(lookbookImageId);
    }

    @Override
    public Page<ImageInspection> getByStatuses(List<InspectionStatus> statuses, Pageable pageable) {
        return imageInspectionJpaRepository.findByStatusIn(statuses, pageable);
    }

    @Override
    public ImageInspection save(ImageInspection imageInspection) {
        return imageInspectionJpaRepository.save(imageInspection);
    }
}
