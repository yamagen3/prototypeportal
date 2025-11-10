package com.prototypeportal.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ContractOption エンティティのテスト
 */
@DisplayName("ContractOption エンティティのテスト")
class ContractOptionTest {

    private ContractOption contractOption;
    private PlanOption planOption;

    @BeforeEach
    void setUp() {
        planOption = PlanOption.builder()
            .id(UUID.randomUUID())
            .optionCode("OPT001")
            .optionName("Test Option")
            .price(BigDecimal.valueOf(1000))
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        contractOption = ContractOption.builder()
            .id(UUID.randomUUID())
            .planOption(planOption)
            .quantity(2)
            .price(BigDecimal.valueOf(1000))
            .rewardAmount(BigDecimal.valueOf(500))
            .build();
    }

    @Test
    @DisplayName("getTotalRewardAmount()は、報酬額 × 数量を返す")
    void shouldGetTotalRewardAmount() {
        // 報酬額500 × 数量2 = 1000
        BigDecimal total = contractOption.getTotalRewardAmount();

        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("getTotalPrice()は、価格 × 数量を返す")
    void shouldGetTotalPrice() {
        // 価格1000 × 数量2 = 2000
        BigDecimal total = contractOption.getTotalPrice();

        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(2000));
    }

    @Test
    @DisplayName("snapshotPrices()は、PlanOptionから価格と報酬額をコピーする")
    void shouldSnapshotPricesFromPlanOption() {
        PlanOption newPlanOption = PlanOption.builder()
            .id(UUID.randomUUID())
            .optionCode("OPT002")
            .optionName("New Option")
            .price(BigDecimal.valueOf(2000))
            .rewardAmount(BigDecimal.valueOf(800))
            .build();

        contractOption.snapshotPrices(newPlanOption);

        assertThat(contractOption.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(2000));
        assertThat(contractOption.getRewardAmount()).isEqualByComparingTo(BigDecimal.valueOf(800));
    }

    @Test
    @DisplayName("数量が1の場合、getTotalRewardAmount()は報酬額と同じ")
    void shouldReturnSameRewardAmountWhenQuantityIsOne() {
        contractOption.setQuantity(1);

        BigDecimal total = contractOption.getTotalRewardAmount();

        assertThat(total).isEqualByComparingTo(contractOption.getRewardAmount());
    }

    @Test
    @DisplayName("数量が1の場合、getTotalPrice()は価格と同じ")
    void shouldReturnSamePriceWhenQuantityIsOne() {
        contractOption.setQuantity(1);

        BigDecimal total = contractOption.getTotalPrice();

        assertThat(total).isEqualByComparingTo(contractOption.getPrice());
    }

    @Test
    @DisplayName("数量が5の場合、getTotalRewardAmount()は報酬額 × 5")
    void shouldCalculateTotalRewardAmountWithQuantityFive() {
        contractOption.setQuantity(5);

        BigDecimal total = contractOption.getTotalRewardAmount();

        // 500 × 5 = 2500
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(2500));
    }
}
