package com.prototypeportal.dto;

import com.prototypeportal.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * プラン情報レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponseDto {

    private UUID id;
    private String planCode;
    private String planName;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal baseReward;
    private Boolean isActive;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PlanOptionResponseDto> options;

    /**
     * Planエンティティから変換
     */
    public static PlanResponseDto fromEntity(Plan plan) {
        return PlanResponseDto.builder()
            .id(plan.getId())
            .planCode(plan.getPlanCode())
            .planName(plan.getPlanName())
            .description(plan.getDescription())
            .basePrice(plan.getBasePrice())
            .baseReward(plan.getBaseReward())
            .isActive(plan.getIsActive())
            .displayOrder(plan.getDisplayOrder())
            .createdAt(plan.getCreatedAt())
            .updatedAt(plan.getUpdatedAt())
            .options(plan.getOptions().stream()
                .map(PlanOptionResponseDto::fromEntity)
                .collect(Collectors.toList()))
            .build();
    }

    /**
     * Planエンティティから変換（オプションなし）
     */
    public static PlanResponseDto fromEntityWithoutOptions(Plan plan) {
        return PlanResponseDto.builder()
            .id(plan.getId())
            .planCode(plan.getPlanCode())
            .planName(plan.getPlanName())
            .description(plan.getDescription())
            .basePrice(plan.getBasePrice())
            .baseReward(plan.getBaseReward())
            .isActive(plan.getIsActive())
            .displayOrder(plan.getDisplayOrder())
            .createdAt(plan.getCreatedAt())
            .updatedAt(plan.getUpdatedAt())
            .build();
    }
}
