package com.prototypeportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 報酬シミュレーションレスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardSimulationResponseDto {

    private UUID planId;
    private String planName;
    private BigDecimal planBaseReward;
    @Builder.Default
    private List<RewardSimulationOptionResultDto> options = new ArrayList<>();
    private BigDecimal totalOptionsReward;
    private BigDecimal totalReward;
}
