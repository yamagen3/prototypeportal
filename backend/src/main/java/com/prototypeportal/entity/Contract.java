package com.prototypeportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 契約エンティティ
 */
@Entity
@Table(name = "contracts", indexes = {
    @Index(name = "idx_contracts_user_id", columnList = "user_id"),
    @Index(name = "idx_contracts_plan_id", columnList = "plan_id"),
    @Index(name = "idx_contracts_status", columnList = "status"),
    @Index(name = "idx_contracts_reward_status", columnList = "reward_status"),
    @Index(name = "idx_contracts_application_number", columnList = "application_number"),
    @Index(name = "idx_contracts_external_approval_id", columnList = "external_approval_id"),
    @Index(name = "idx_contracts_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contracts_user"))
    @NotNull(message = "User is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contracts_plan"))
    @NotNull(message = "Plan is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Plan plan;

    @NotBlank(message = "Application number is required")
    @Size(max = 50, message = "Application number must not exceed 50 characters")
    @Column(name = "application_number", nullable = false, unique = true, length = 50)
    private String applicationNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ContractStatus status = ContractStatus.DRAFT;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Size(max = 100, message = "External approval ID must not exceed 100 characters")
    @Column(name = "external_approval_id", length = 100)
    private String externalApprovalId;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @NotNull(message = "Total reward amount is required")
    @PositiveOrZero(message = "Total reward amount must be zero or positive")
    @Column(name = "total_reward_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalRewardAmount = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reward_status", nullable = false)
    @Builder.Default
    private RewardStatus rewardStatus = RewardStatus.PENDING;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContractOption> contractOptions = new ArrayList<>();

    /**
     * 契約が下書き状態かチェック
     */
    public boolean isDraft() {
        return this.status == ContractStatus.DRAFT;
    }

    /**
     * 契約が申込済みかチェック
     */
    public boolean isSubmitted() {
        return this.status == ContractStatus.SUBMITTED;
    }

    /**
     * 契約が承認済みかチェック
     */
    public boolean isApproved() {
        return this.status == ContractStatus.APPROVED;
    }

    /**
     * 契約が有効かチェック
     */
    public boolean isActive() {
        return this.status == ContractStatus.ACTIVE;
    }

    /**
     * 契約を申し込む
     */
    public void submit() {
        if (this.status != ContractStatus.DRAFT && this.status != ContractStatus.PENDING) {
            throw new IllegalStateException("Contract can only be submitted from DRAFT or PENDING status");
        }
        this.status = ContractStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 契約を承認する
     */
    public void approve(String externalApprovalId) {
        if (this.status != ContractStatus.SUBMITTED) {
            throw new IllegalStateException("Contract can only be approved from SUBMITTED status");
        }
        this.status = ContractStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.externalApprovalId = externalApprovalId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 契約を却下する
     */
    public void reject(String reason) {
        if (this.status != ContractStatus.SUBMITTED) {
            throw new IllegalStateException("Contract can only be rejected from SUBMITTED status");
        }
        this.status = ContractStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.rejectionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 契約を有効化する（承認後）
     */
    public void activate(LocalDate startDate, LocalDate endDate) {
        if (this.status != ContractStatus.APPROVED) {
            throw new IllegalStateException("Contract can only be activated from APPROVED status");
        }
        this.status = ContractStatus.ACTIVE;
        this.contractStartDate = startDate;
        this.contractEndDate = endDate;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 契約をキャンセルする
     */
    public void cancel() {
        if (this.status == ContractStatus.CANCELLED) {
            throw new IllegalStateException("Contract is already cancelled");
        }
        this.status = ContractStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 報酬ステータスを確定済みにする
     */
    public void confirmReward() {
        if (this.rewardStatus == RewardStatus.PAID) {
            throw new IllegalStateException("Reward is already paid");
        }
        this.rewardStatus = RewardStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 報酬ステータスを支払済みにする
     */
    public void markRewardAsPaid() {
        if (this.rewardStatus != RewardStatus.CONFIRMED) {
            throw new IllegalStateException("Reward must be confirmed before marking as paid");
        }
        this.rewardStatus = RewardStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 契約オプションを追加
     */
    public void addContractOption(ContractOption contractOption) {
        contractOptions.add(contractOption);
        contractOption.setContract(this);
    }

    /**
     * 契約オプションを削除
     */
    public void removeContractOption(ContractOption contractOption) {
        contractOptions.remove(contractOption);
        contractOption.setContract(null);
    }

    /**
     * 合計報酬額を計算して設定
     */
    public void calculateTotalRewardAmount(BigDecimal planBaseReward) {
        BigDecimal optionsReward = contractOptions.stream()
            .map(ContractOption::getTotalRewardAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalRewardAmount = planBaseReward.add(optionsReward);
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = ContractStatus.DRAFT;
        }
        if (this.rewardStatus == null) {
            this.rewardStatus = RewardStatus.PENDING;
        }
        if (this.totalRewardAmount == null) {
            this.totalRewardAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
