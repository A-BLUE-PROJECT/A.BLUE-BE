package com.allblue.admin.domain.model;

import com.allblue.admin.domain.exception.AdminBusinessException;
import com.allblue.admin.domain.exception.AdminErrorCode;
import com.allblue.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
        name = "image_inspections",
        indexes = {
            @Index(name = "idx_image_inspections_lookbook_image", columnList = "lookbookImageId"),
            @Index(name = "idx_image_inspections_status", columnList = "status")
        })
@SQLDelete(sql = "UPDATE image_inspections SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageInspection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lookbookImageId;

    @Column(nullable = false, columnDefinition = "text")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InspectionStatus status = InspectionStatus.PENDING;

    @Column(name = "ai_comment", length = 500)
    private String aiComment;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(nullable = false)
    private boolean deleted = false;

    private ImageInspection(Long lookbookImageId, String imageUrl) {
        this.lookbookImageId = lookbookImageId;
        this.imageUrl = imageUrl;
    }

    public static ImageInspection create(Long lookbookImageId, String imageUrl) {
        return new ImageInspection(lookbookImageId, imageUrl);
    }

    public void updateAiResult(InspectionStatus targetStatus, String aiComment) {
        if (this.status == targetStatus) {
            log.debug("중복 AI 콜백 무시 - inspectionId: {}, status: {}", this.id, this.status);
            return;
        }
        if (!targetStatus.isAiResult()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_TARGET_INSPECTION_STATUS);
        }
        if (!this.status.isProcessableByAi()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_INSPECTION_STATUS_TRANSITION);
        }
        this.status = targetStatus;
        this.aiComment = aiComment;
    }

    public void updateAdminStatus(InspectionStatus targetStatus, Long adminId) {
        if (this.status == targetStatus) {
            log.debug("중복 관리자 ?�태 변�?무시 - inspectionId: {}, status: {}", this.id, this.status);
            return;
        }
        if (!this.status.isProcessableByAdmin()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_INSPECTION_STATUS_TRANSITION);
        }
        if (!targetStatus.isAdminResult()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_TARGET_INSPECTION_STATUS);
        }
        this.status = targetStatus;
        this.adminId = adminId;
    }
}
