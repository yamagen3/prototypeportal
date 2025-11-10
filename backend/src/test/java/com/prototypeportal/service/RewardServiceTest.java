package com.prototypeportal.service;

import com.prototypeportal.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * RewardServiceのテスト
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RewardService のテスト")
class RewardServiceTest {

    @InjectMocks
    private RewardService rewardService;

    private Plan plan;
    private List<PlanOption> planOptions;

    @BeforeEach
    void setUp() {
        plan = Plan.builder()
            .id(UUID.randomUUID())
            .planCode("PLAN001")
            .planName("Standard Plan")
            .baseReward(BigDecimal.valueOf(1000))
            .build();

        planOptions = new ArrayList<>();
        planOptions.add(PlanOption.builder()
            .id(UUID.randomUUID())
            .plan(plan)
            .optionCode("OPT001")
            .optionName("Option 1")
            .rewardAmount(BigDecimal.valueOf(500))
            .build());

        planOptions.add(PlanOption.builder()
            .id(UUID.randomUUID())
            .plan(plan)
            .optionCode("OPT002")
            .optionName("Option 2")
            .rewardAmount(BigDecimal.valueOf(300))
            .build());
    }

    @Test
    @DisplayName("プラン基本報酬のみの場合、合計報酬はプラン基本報酬と同じ")
    void shouldReturnPlanBaseRewardWhenNoOptions() {
        List<ContractOption> contractOptions = new ArrayList<>();

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("オプションが1つの場合、合計報酬 = プラン基本報酬 + オプション報酬 × 数量")
    void shouldCalculateTotalRewardWithSingleOption() {
        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(2)
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contractOptions.add(contractOption1);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 1000 (プラン基本) + 500 × 2 (オプション) = 2000
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(2000));
    }

    @Test
    @DisplayName("複数オプションの場合、合計報酬 = プラン基本報酬 + Σ(オプション報酬 × 数量)")
    void shouldCalculateTotalRewardWithMultipleOptions() {
        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(2)
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        ContractOption contractOption2 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(1))
            .quantity(3)
            .rewardAmount(BigDecimal.valueOf(300))
            .build();

        contractOptions.add(contractOption1);
        contractOptions.add(contractOption2);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 1000 (プラン基本) + 500 × 2 (オプション1) + 300 × 3 (オプション2) = 2900
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(2900));
    }

    @Test
    @DisplayName("数量が0のオプションは報酬計算に含まれない")
    void shouldNotIncludeOptionWithZeroQuantity() {
        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(0)
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contractOptions.add(contractOption1);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 1000 (プラン基本) + 0 = 1000
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("報酬額が0のオプションでも数量は計算される")
    void shouldCalculateOptionWithZeroReward() {
        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(5)
            .rewardAmount(BigDecimal.ZERO)
            .build();

        contractOptions.add(contractOption1);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 1000 (プラン基本) + 0 × 5 = 1000
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("プラン基本報酬が0の場合でもオプション報酬は計算される")
    void shouldCalculateOptionsWhenPlanBaseRewardIsZero() {
        plan.setBaseReward(BigDecimal.ZERO);

        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(2)
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contractOptions.add(contractOption1);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 0 (プラン基本) + 500 × 2 (オプション) = 1000
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("contractOptionsがnullの場合、プラン基本報酬のみを返す")
    void shouldReturnPlanBaseRewardWhenContractOptionsIsNull() {
        BigDecimal totalReward = rewardService.calculateTotalReward(plan, null);

        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("大きな数量でも正確に計算される")
    void shouldCalculateAccuratelyWithLargeQuantity() {
        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(100)
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contractOptions.add(contractOption1);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 1000 (プラン基本) + 500 × 100 (オプション) = 51000
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(51000));
    }

    @Test
    @DisplayName("小数点を含む報酬額でも正確に計算される")
    void shouldCalculateAccuratelyWithDecimalReward() {
        List<ContractOption> contractOptions = new ArrayList<>();

        ContractOption contractOption1 = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOptions.get(0))
            .quantity(3)
            .rewardAmount(BigDecimal.valueOf(123.45))
            .build();

        contractOptions.add(contractOption1);

        BigDecimal totalReward = rewardService.calculateTotalReward(plan, contractOptions);

        // 1000 (プラン基本) + 123.45 × 3 (オプション) = 1370.35
        assertThat(totalReward).isEqualByComparingTo(BigDecimal.valueOf(1370.35));
    }
}
