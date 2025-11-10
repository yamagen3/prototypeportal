package com.prototypeportal.service;

import com.prototypeportal.entity.ContractOption;
import com.prototypeportal.entity.Plan;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;

/**
 * 報酬計算サービス
 */
@ApplicationScoped
public class RewardService {

    /**
     * 合計報酬額を計算
     *
     * 計算式: 合計報酬 = プラン基本報酬 + Σ(選択オプション報酬 × 数量)
     *
     * @param plan プラン
     * @param contractOptions 契約オプションリスト
     * @return 合計報酬額
     */
    public BigDecimal calculateTotalReward(Plan plan, List<ContractOption> contractOptions) {
        // プラン基本報酬
        BigDecimal totalReward = plan.getBaseReward();

        // オプション報酬の合計を加算
        if (contractOptions != null && !contractOptions.isEmpty()) {
            BigDecimal optionsReward = contractOptions.stream()
                .map(ContractOption::getTotalRewardAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalReward = totalReward.add(optionsReward);
        }

        return totalReward;
    }
}
