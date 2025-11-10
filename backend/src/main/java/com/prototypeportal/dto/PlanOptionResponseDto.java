package com.prototypeportal.dto;

import com.prototypeportal.entity.PlanOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * プランオプション情報レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanOptionResponseDto {

    private UUID id;
    private UUID planId;
    private String optionCode;
    private String optionName;
    private String description;
    private BigDecimal price;
    private BigDecimal rewardAmount;
    private Boolean isRequired;
    private Boolean isActive;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * PlanOptionエンティティから変換
     */
    public static PlanOptionResponseDto fromEntity(PlanOption option) {
        return PlanOptionResponseDto.builder()
            .id(option.getId())
            .planId(option.getPlan().getId())
            .optionCode(option.getOptionCode())
            .optionName(option.getOptionName())
            .description(option.getDescription())
            .price(option.getPrice())
            .rewardAmount(option.getRewardAmount())
            .isRequired(option.getIsRequired())
            .isActive(option.getIsActive())
            .displayOrder(option.getDisplayOrder())
            .createdAt(option.getCreatedAt())
            .updatedAt(option.getUpdatedAt())
            .build();
    }
}
