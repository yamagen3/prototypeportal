package com.prototypeportal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 報酬シミュレーションオプションDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardSimulationOptionDto {

    @NotNull(message = "Plan option ID is required")
    private UUID planOptionId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Builder.Default
    private Integer quantity = 1;
}
