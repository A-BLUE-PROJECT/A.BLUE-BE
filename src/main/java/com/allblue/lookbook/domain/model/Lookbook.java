package com.allblue.lookbook.domain.model;

import com.allblue.common.entity.BaseTimeEntity;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.enums.LookbookStatus;
import com.allblue.lookbook.domain.model.enums.Position;
import com.allblue.lookbook.domain.model.enums.Season;
import com.allblue.lookbook.domain.model.enums.StyleType;
import com.allblue.lookbook.domain.model.enums.TargetGender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "lookbooks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE lookbooks SET deleted_at = now() WHERE id = ?")
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class Lookbook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "style_type", nullable = false)
    private StyleType styleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false)
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_gender")
    private TargetGender targetGender;

    @Column(name = "tags")
    private String tags;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LookbookStatus status;

    @Column(name = "ai_score")
    private Integer aiScore;

    @OneToOne(mappedBy = "lookbook", cascade = CascadeType.ALL, orphanRemoval = true)
    private LookbookImage lookbookImage;

    @OneToMany(mappedBy = "lookbook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LookbookItem> lookbookItems = new ArrayList<>();

    private Lookbook(StyleType styleType, Season season, TargetGender targetGender, String tags) {
        this.styleType = styleType;
        this.season = season;
        this.targetGender = targetGender;
        this.tags = tags;
        this.status = LookbookStatus.PENDING;
    }

    public static Lookbook create(
            StyleType styleType,
            Season season,
            TargetGender targetGender,
            String tags,
            List<LookbookItemInfo> items) {
        Lookbook lookbook = new Lookbook(styleType, season, targetGender, tags);
        items.stream()
                .map(item -> LookbookItem.create(lookbook, item.productId(), item.position()))
                .forEach(lookbook.lookbookItems::add);
        return lookbook;
    }

    public void complete(String originUrl, String imageUrl, Integer aiScore) {
        validateStatusIsPending();
        this.lookbookImage = LookbookImage.create(this, originUrl, imageUrl);
        this.status = LookbookStatus.COMPLETED;
        this.aiScore = aiScore;
    }

    public void fail() {
        validateStatusIsPending();
        this.status = LookbookStatus.FAILED;
    }

    public void approve() {
        if (this.status != LookbookStatus.COMPLETED) {
            throw new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_STATUS_NOT_COMPLETED);
        }
        this.status = LookbookStatus.APPROVED;
    }

    public void reject() {
        if (this.status != LookbookStatus.COMPLETED) {
            throw new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_STATUS_NOT_COMPLETED);
        }
        this.status = LookbookStatus.REJECTED;
    }

    private void validateStatusIsPending() {
        if (this.status != LookbookStatus.PENDING) {
            throw new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_STATUS_NOT_PENDING);
        }
    }

    public record LookbookItemInfo(Long productId, Position position) {}
}
