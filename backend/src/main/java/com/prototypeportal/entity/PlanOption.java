package com.prototypeportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * プランオプションエンティティ
 */
@Entity
@Table(name = "plan_options", indexes = {
    @Index(name = "idx_plan_options_plan_id", columnList = "plan_id"),
    @Index(name = "idx_plan_options_is_active", columnList = "is_active"),
    @Index(name = "idx_plan_options_display_order", columnList = "display_order")
}, uniqueConstraints = {
    @UniqueConstraint(name = "idx_plan_options_unique_code", columnNames = {"plan_id", "option_code"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_options_plan"))
    @NotNull(message = "Plan is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Plan plan;

    @NotBlank(message = "Option code is required")
    @Size(max = 50, message = "Option code must not exceed 50 characters")
    @Column(name = "option_code", nullable = false, length = 50)
    private String optionCode;

    @NotBlank(message = "Option name is required")
    @Size(max = 255, message = "Option name must not exceed 255 characters")
    @Column(name = "option_name", nullable = false, length = 255)
    private String optionName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @PositiveOrZero(message = "Price must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Reward amount is required")
    @PositiveOrZero(message = "Reward amount must be zero or positive")
    @Column(name = "reward_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal rewardAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "is_required", nullable = false)
    @Builder.Default
    private Boolean isRequired = false;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @NotNull
    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * オプションがアクティブかチェック
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    /**
     * オプションが必須かチェック
     */
    public boolean isRequired() {
        return Boolean.TRUE.equals(isRequired);
    }

    /**
     * オプションを有効化
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * オプションを無効化
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.isRequired == null) {
            this.isRequired = false;
        }
        if (this.displayOrder == null) {
            this.displayOrder = 0;
        }
        if (this.rewardAmount == null) {
            this.rewardAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
