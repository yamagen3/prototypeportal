package com.prototypeportal.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * Contract エンティティのテスト
 */
@DisplayName("Contract エンティティのテスト")
class ContractTest {

    private Contract contract;
    private User user;
    private Plan plan;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .name("Test User")
            .passwordHash("hashed_password")
            .build();

        plan = Plan.builder()
            .id(UUID.randomUUID())
            .planCode("PLAN001")
            .planName("Test Plan")
            .baseReward(BigDecimal.valueOf(1000))
            .build();

        contract = Contract.builder()
            .id(UUID.randomUUID())
            .user(user)
            .plan(plan)
            .applicationNumber("APP-20250101-001")
            .status(ContractStatus.DRAFT)
            .rewardStatus(RewardStatus.PENDING)
            .totalRewardAmount(BigDecimal.ZERO)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("isDraft()は、statusがDRAFTの時にtrueを返す")
    void shouldReturnTrueWhenStatusIsDraft() {
        contract.setStatus(ContractStatus.DRAFT);
        assertThat(contract.isDraft()).isTrue();

        contract.setStatus(ContractStatus.SUBMITTED);
        assertThat(contract.isDraft()).isFalse();
    }

    @Test
    @DisplayName("isSubmitted()は、statusがSUBMITTEDの時にtrueを返す")
    void shouldReturnTrueWhenStatusIsSubmitted() {
        contract.setStatus(ContractStatus.SUBMITTED);
        assertThat(contract.isSubmitted()).isTrue();

        contract.setStatus(ContractStatus.DRAFT);
        assertThat(contract.isSubmitted()).isFalse();
    }

    @Test
    @DisplayName("isApproved()は、statusがAPPROVEDの時にtrueを返す")
    void shouldReturnTrueWhenStatusIsApproved() {
        contract.setStatus(ContractStatus.APPROVED);
        assertThat(contract.isApproved()).isTrue();

        contract.setStatus(ContractStatus.DRAFT);
        assertThat(contract.isApproved()).isFalse();
    }

    @Test
    @DisplayName("isActive()は、statusがACTIVEの時にtrueを返す")
    void shouldReturnTrueWhenStatusIsActive() {
        contract.setStatus(ContractStatus.ACTIVE);
        assertThat(contract.isActive()).isTrue();

        contract.setStatus(ContractStatus.DRAFT);
        assertThat(contract.isActive()).isFalse();
    }

    @Test
    @DisplayName("submit()は、DRAFTステータスから契約を申し込める")
    void shouldSubmitContractFromDraftStatus() {
        contract.setStatus(ContractStatus.DRAFT);
        LocalDateTime beforeSubmit = LocalDateTime.now().minusSeconds(1);

        contract.submit();

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.SUBMITTED);
        assertThat(contract.getSubmittedAt()).isNotNull();
        assertThat(contract.getSubmittedAt()).isAfterOrEqualTo(beforeSubmit);
    }

    @Test
    @DisplayName("submit()は、PENDINGステータスから契約を申し込める")
    void shouldSubmitContractFromPendingStatus() {
        contract.setStatus(ContractStatus.PENDING);

        contract.submit();

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.SUBMITTED);
        assertThat(contract.getSubmittedAt()).isNotNull();
    }

    @Test
    @DisplayName("submit()は、DRAFT/PENDING以外のステータスから申し込むと例外を投げる")
    void shouldThrowExceptionWhenSubmitFromInvalidStatus() {
        contract.setStatus(ContractStatus.SUBMITTED);

        assertThatThrownBy(() -> contract.submit())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Contract can only be submitted from DRAFT or PENDING status");
    }

    @Test
    @DisplayName("approve()は、SUBMITTEDステータスから契約を承認できる")
    void shouldApproveContractFromSubmittedStatus() {
        contract.setStatus(ContractStatus.SUBMITTED);
        String externalId = "EXT-12345";
        LocalDateTime beforeApprove = LocalDateTime.now().minusSeconds(1);

        contract.approve(externalId);

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.APPROVED);
        assertThat(contract.getApprovedAt()).isNotNull();
        assertThat(contract.getApprovedAt()).isAfterOrEqualTo(beforeApprove);
        assertThat(contract.getExternalApprovalId()).isEqualTo(externalId);
    }

    @Test
    @DisplayName("approve()は、SUBMITTED以外のステータスから承認すると例外を投げる")
    void shouldThrowExceptionWhenApproveFromInvalidStatus() {
        contract.setStatus(ContractStatus.DRAFT);

        assertThatThrownBy(() -> contract.approve("EXT-12345"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Contract can only be approved from SUBMITTED status");
    }

    @Test
    @DisplayName("reject()は、SUBMITTEDステータスから契約を却下できる")
    void shouldRejectContractFromSubmittedStatus() {
        contract.setStatus(ContractStatus.SUBMITTED);
        String reason = "書類不備";
        LocalDateTime beforeReject = LocalDateTime.now().minusSeconds(1);

        contract.reject(reason);

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.REJECTED);
        assertThat(contract.getRejectedAt()).isNotNull();
        assertThat(contract.getRejectedAt()).isAfterOrEqualTo(beforeReject);
        assertThat(contract.getRejectionReason()).isEqualTo(reason);
    }

    @Test
    @DisplayName("reject()は、SUBMITTED以外のステータスから却下すると例外を投げる")
    void shouldThrowExceptionWhenRejectFromInvalidStatus() {
        contract.setStatus(ContractStatus.DRAFT);

        assertThatThrownBy(() -> contract.reject("理由"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Contract can only be rejected from SUBMITTED status");
    }

    @Test
    @DisplayName("activate()は、APPROVEDステータスから契約を有効化できる")
    void shouldActivateContractFromApprovedStatus() {
        contract.setStatus(ContractStatus.APPROVED);
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        contract.activate(startDate, endDate);

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.ACTIVE);
        assertThat(contract.getContractStartDate()).isEqualTo(startDate);
        assertThat(contract.getContractEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("activate()は、APPROVED以外のステータスから有効化すると例外を投げる")
    void shouldThrowExceptionWhenActivateFromInvalidStatus() {
        contract.setStatus(ContractStatus.DRAFT);
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        assertThatThrownBy(() -> contract.activate(startDate, endDate))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Contract can only be activated from APPROVED status");
    }

    @Test
    @DisplayName("cancel()は、契約をキャンセルできる")
    void shouldCancelContract() {
        contract.setStatus(ContractStatus.ACTIVE);

        contract.cancel();

        assertThat(contract.getStatus()).isEqualTo(ContractStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel()は、既にキャンセル済みの契約をキャンセルすると例外を投げる")
    void shouldThrowExceptionWhenCancelAlreadyCancelledContract() {
        contract.setStatus(ContractStatus.CANCELLED);

        assertThatThrownBy(() -> contract.cancel())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Contract is already cancelled");
    }

    @Test
    @DisplayName("confirmReward()は、報酬を確定済みにできる")
    void shouldConfirmReward() {
        contract.setRewardStatus(RewardStatus.PENDING);

        contract.confirmReward();

        assertThat(contract.getRewardStatus()).isEqualTo(RewardStatus.CONFIRMED);
    }

    @Test
    @DisplayName("confirmReward()は、既に支払済みの報酬を確定すると例外を投げる")
    void shouldThrowExceptionWhenConfirmAlreadyPaidReward() {
        contract.setRewardStatus(RewardStatus.PAID);

        assertThatThrownBy(() -> contract.confirmReward())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Reward is already paid");
    }

    @Test
    @DisplayName("markRewardAsPaid()は、確定済み報酬を支払済みにできる")
    void shouldMarkRewardAsPaidFromConfirmedStatus() {
        contract.setRewardStatus(RewardStatus.CONFIRMED);

        contract.markRewardAsPaid();

        assertThat(contract.getRewardStatus()).isEqualTo(RewardStatus.PAID);
    }

    @Test
    @DisplayName("markRewardAsPaid()は、確定済み以外の報酬を支払済みにすると例外を投げる")
    void shouldThrowExceptionWhenMarkRewardAsPaidFromInvalidStatus() {
        contract.setRewardStatus(RewardStatus.PENDING);

        assertThatThrownBy(() -> contract.markRewardAsPaid())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Reward must be confirmed before marking as paid");
    }

    @Test
    @DisplayName("calculateTotalRewardAmount()は、プラン基本報酬とオプション報酬の合計を計算する")
    void shouldCalculateTotalRewardAmount() {
        BigDecimal planBaseReward = BigDecimal.valueOf(1000);

        // オプション1: 報酬500 × 数量2 = 1000
        PlanOption option1 = PlanOption.builder()
            .id(UUID.randomUUID())
            .optionCode("OPT001")
            .optionName("Option 1")
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(option1)
            .quantity(2)
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        // オプション2: 報酬300 × 数量1 = 300
        PlanOption option2 = PlanOption.builder()
            .id(UUID.randomUUID())
            .optionCode("OPT002")
            .optionName("Option 2")
            .rewardAmount(BigDecimal.valueOf(300))
            .build();

        ContractOption contractOption2 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(option2)
            .quantity(1)
            .rewardAmount(BigDecimal.valueOf(300))
            .build();

        contract.addContractOption(contractOption1);
        contract.addContractOption(contractOption2);

        contract.calculateTotalRewardAmount(planBaseReward);

        // 合計 = 1000 (プラン) + 1000 (オプション1) + 300 (オプション2) = 2300
        assertThat(contract.getTotalRewardAmount()).isEqualByComparingTo(BigDecimal.valueOf(2300));
    }

    @Test
    @DisplayName("addContractOption()は、契約オプションを追加できる")
    void shouldAddContractOption() {
        ContractOption contractOption = ContractOption.builder()
            .id(UUID.randomUUID())
            .quantity(1)
            .price(BigDecimal.valueOf(1000))
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contract.addContractOption(contractOption);

        assertThat(contract.getContractOptions()).hasSize(1);
        assertThat(contract.getContractOptions()).contains(contractOption);
        assertThat(contractOption.getContract()).isEqualTo(contract);
    }

    @Test
    @DisplayName("removeContractOption()は、契約オプションを削除できる")
    void shouldRemoveContractOption() {
        ContractOption contractOption = ContractOption.builder()
            .id(UUID.randomUUID())
            .quantity(1)
            .price(BigDecimal.valueOf(1000))
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contract.addContractOption(contractOption);
        contract.removeContractOption(contractOption);

        assertThat(contract.getContractOptions()).isEmpty();
        assertThat(contractOption.getContract()).isNull();
    }
}
