package com.allblue.admin.application.dto.result;

import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.model.InspectionStatus;
import java.time.LocalDateTime;

public record ImageInspectionResult(
        Long id,
        Long lookbookImageId,
        String imageUrl,
        InspectionStatus status,
        String aiComment,
        Long adminId,
        LocalDateTime createdAt) {
    public static ImageInspectionResult from(ImageInspection entity) {
        return new ImageInspectionResult(
                entity.getId(),
                entity.getLookbookImageId(),
                entity.getImageUrl(),
                entity.getStatus(),
                entity.getAiComment(),
                entity.getAdminId(),
                entity.getCreatedAt());
    }
}
