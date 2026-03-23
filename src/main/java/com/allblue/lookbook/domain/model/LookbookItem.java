package com.allblue.lookbook.domain.model;

import com.allblue.lookbook.domain.model.enums.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "lookbook_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LookbookItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookbook_id", nullable = false)
    private Lookbook lookbook;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private Position position;

    private LookbookItem(Lookbook lookbook, Long productId, Position position) {
        this.lookbook = lookbook;
        this.productId = productId;
        this.position = position;
    }

    static LookbookItem create(Lookbook lookbook, Long productId, Position position) {
        return new LookbookItem(lookbook, productId, position);
    }
}
