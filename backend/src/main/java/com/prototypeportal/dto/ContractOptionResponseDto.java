package com.prototypeportal.dto;

import com.prototypeportal.entity.ContractOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 契約オプション情報レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractOptionResponseDto {

    private UUID id;
    private UUID contractId;
    private UUID planOptionId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal rewardAmount;
    private BigDecimal totalPrice;
    private BigDecimal totalRewardAmount;
    private LocalDateTime createdAt;

    /**
     * ContractOptionエンティティから変換
     */
    public static ContractOptionResponseDto fromEntity(ContractOption option) {
        return ContractOptionResponseDto.builder()
            .id(option.getId())
            .contractId(option.getContract().getId())
            .planOptionId(option.getPlanOption().getId())
            .quantity(option.getQuantity())
            .price(option.getPrice())
            .rewardAmount(option.getRewardAmount())
            .totalPrice(option.getTotalPrice())
            .totalRewardAmount(option.getTotalRewardAmount())
            .createdAt(option.getCreatedAt())
            .build();
    }
}
