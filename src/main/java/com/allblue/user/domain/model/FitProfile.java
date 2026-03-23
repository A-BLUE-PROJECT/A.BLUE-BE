package com.allblue.user.domain.model;

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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fit_profiles")
public class FitProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "shoulder_width")
    private Integer shoulderWidth;

    @Column(name = "chest_size")
    private Integer chestSize;

    @Column(name = "waist_size")
    private Integer waistSize;

    @Column(name = "hip_size")
    private Integer hipSize;

    private FitProfile(User user, Integer height, Integer weight,
                       Integer shoulderWidth, Integer chestSize,
                       Integer waistSize, Integer hipSize) {
        this.user = user;
        this.height = height;
        this.weight = weight;
        this.shoulderWidth = shoulderWidth;
        this.chestSize = chestSize;
        this.waistSize = waistSize;
        this.hipSize = hipSize;
    }

    public static FitProfile create(User user, Integer height, Integer weight,
                                    Integer shoulderWidth, Integer chestSize,
                                    Integer waistSize, Integer hipSize) {
        return new FitProfile(user, height, weight, shoulderWidth, chestSize, waistSize, hipSize);
    }

    public void update(Integer height, Integer weight,
                       Integer shoulderWidth, Integer chestSize,
                       Integer waistSize, Integer hipSize) {
        if (height != null) this.height = height;
        if (weight != null) this.weight = weight;
        if (shoulderWidth != null) this.shoulderWidth = shoulderWidth;
        if (chestSize != null) this.chestSize = chestSize;
        if (waistSize != null) this.waistSize = waistSize;
        if (hipSize != null) this.hipSize = hipSize;
    }
}
