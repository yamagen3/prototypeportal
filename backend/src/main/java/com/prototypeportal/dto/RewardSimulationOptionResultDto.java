package com.prototypeportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 報酬シミュレーションオプション結果DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardSimulationOptionResultDto {

    private UUID planOptionId;
    private String optionName;
    private Integer quantity;
    private BigDecimal unitReward;
    private BigDecimal totalReward;
}
