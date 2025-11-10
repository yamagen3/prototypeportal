package com.prototypeportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 報酬シミュレーションリクエストDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardSimulationRequestDto {

    @NotNull(message = "Plan ID is required")
    private UUID planId;

    @Builder.Default
    private List<RewardSimulationOptionDto> options = new ArrayList<>();
}
