package com.prototypeportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 契約オプションエンティティ
 */
@Entity
@Table(name = "contract_options", indexes = {
    @Index(name = "idx_contract_options_contract_id", columnList = "contract_id"),
    @Index(name = "idx_contract_options_plan_option_id", columnList = "plan_option_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contract_options_contract"))
    @NotNull(message = "Contract is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_option_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contract_options_plan_option"))
    @NotNull(message = "Plan option is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PlanOption planOption;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Reward amount is required")
    @PositiveOrZero(message = "Reward amount must be zero or positive")
    @Column(name = "reward_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal rewardAmount;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 合計報酬額を計算（数量 × 報酬額）
     */
    public BigDecimal getTotalRewardAmount() {
        return rewardAmount.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * 合計価格を計算（数量 × 価格）
     */
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * PlanOptionから価格と報酬額を設定（スナップショット）
     */
    public void snapshotPrices(PlanOption planOption) {
        this.price = planOption.getPrice();
        this.rewardAmount = planOption.getRewardAmount();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.quantity == null) {
            this.quantity = 1;
        }
    }
}
