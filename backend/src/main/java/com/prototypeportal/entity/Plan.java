package com.prototypeportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * プラン・サービスエンティティ
 */
@Entity
@Table(name = "plans", indexes = {
    @Index(name = "idx_plans_plan_code", columnList = "plan_code"),
    @Index(name = "idx_plans_is_active", columnList = "is_active"),
    @Index(name = "idx_plans_display_order", columnList = "display_order")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @NotBlank(message = "Plan code is required")
    @Size(max = 50, message = "Plan code must not exceed 50 characters")
    @Column(name = "plan_code", nullable = false, unique = true, length = 50)
    private String planCode;

    @NotBlank(message = "Plan name is required")
    @Size(max = 255, message = "Plan name must not exceed 255 characters")
    @Column(name = "plan_name", nullable = false, length = 255)
    private String planName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @PositiveOrZero(message = "Base price must be zero or positive")
    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @NotNull(message = "Base reward is required")
    @PositiveOrZero(message = "Base reward must be zero or positive")
    @Column(name = "base_reward", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseReward;

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

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PlanOption> options = new ArrayList<>();

    /**
     * プランがアクティブかチェック
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    /**
     * プランを有効化
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * プランを無効化
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * オプションを追加
     */
    public void addOption(PlanOption option) {
        options.add(option);
        option.setPlan(this);
    }

    /**
     * オプションを削除
     */
    public void removeOption(PlanOption option) {
        options.remove(option);
        option.setPlan(null);
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.displayOrder == null) {
            this.displayOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
