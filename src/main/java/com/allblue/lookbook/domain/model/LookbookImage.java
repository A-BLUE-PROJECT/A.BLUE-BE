package com.allblue.lookbook.domain.model;

import com.allblue.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "lookbook_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE lookbook_images SET deleted_at = now() WHERE id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class LookbookImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookbook_id", nullable = false, unique = true)
    private Lookbook lookbook;

    @Column(name = "origin_url", columnDefinition = "text")
    private String originUrl;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    private LookbookImage(Lookbook lookbook, String originUrl, String imageUrl) {
        this.lookbook = lookbook;
        this.originUrl = originUrl;
        this.imageUrl = imageUrl;
    }

    public static LookbookImage create(Lookbook lookbook, String originUrl, String imageUrl) {
        return new LookbookImage(lookbook, originUrl, imageUrl);
    }
}
