package com.allblue.product.domain.model;

import com.allblue.common.entity.BaseTimeEntity;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE products SET deleted_at = now() WHERE id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "external_product_id")
    private String externalProductId;

    @Column(name = "raw_category")
    private String rawCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "mapped_category")
    private MappedCategory mappedCategory;

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "sale_price")
    private Integer salePrice;

    @Column(name = "product_image_url", columnDefinition = "text")
    private String productImageUrl;

    @Column(name = "origin_url", columnDefinition = "text")
    private String originUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false)
    private StockStatus stockStatus;

    @Column(name = "hidden", nullable = false)
    private boolean hidden = false;

    private Product(
            Long sellerId,
            String externalProductId,
            String rawCategory,
            MappedCategory mappedCategory,
            String brandName,
            String productName,
            Integer price,
            Integer salePrice,
            String productImageUrl,
            String originUrl,
            StockStatus stockStatus) {
        this.sellerId = sellerId;
        this.externalProductId = externalProductId;
        this.rawCategory = rawCategory;
        this.mappedCategory = mappedCategory;
        this.brandName = brandName;
        this.productName = productName;
        this.price = price;
        this.salePrice = salePrice;
        this.productImageUrl = productImageUrl;
        this.originUrl = originUrl;
        this.stockStatus = stockStatus;
    }

    public static Product create(
            Long sellerId,
            String externalProductId,
            String rawCategory,
            MappedCategory mappedCategory,
            String brandName,
            String productName,
            Integer price,
            Integer salePrice,
            String productImageUrl,
            String originUrl,
            StockStatus stockStatus) {
        return new Product(
                sellerId,
                externalProductId,
                rawCategory,
                mappedCategory,
                brandName,
                productName,
                price,
                salePrice,
                productImageUrl,
                originUrl,
                stockStatus);
    }

    public void updateStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public void hide() {
        this.hidden = true;
    }

    public void reveal() {
        this.hidden = false;
    }
}
