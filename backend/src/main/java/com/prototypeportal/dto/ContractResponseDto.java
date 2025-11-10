package com.prototypeportal.dto;

import com.prototypeportal.entity.Contract;
import com.prototypeportal.entity.ContractStatus;
import com.prototypeportal.entity.RewardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 契約情報レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDto {

    private UUID id;
    private UUID userId;
    private UUID planId;
    private String applicationNumber;
    private ContractStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private String externalApprovalId;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private BigDecimal totalRewardAmount;
    private RewardStatus rewardStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ContractOptionResponseDto> options;

    /**
     * Contractエンティティから変換
     */
    public static ContractResponseDto fromEntity(Contract contract) {
        return ContractResponseDto.builder()
            .id(contract.getId())
            .userId(contract.getUser().getId())
            .planId(contract.getPlan().getId())
            .applicationNumber(contract.getApplicationNumber())
            .status(contract.getStatus())
            .submittedAt(contract.getSubmittedAt())
            .approvedAt(contract.getApprovedAt())
            .rejectedAt(contract.getRejectedAt())
            .rejectionReason(contract.getRejectionReason())
            .externalApprovalId(contract.getExternalApprovalId())
            .contractStartDate(contract.getContractStartDate())
            .contractEndDate(contract.getContractEndDate())
            .totalRewardAmount(contract.getTotalRewardAmount())
            .rewardStatus(contract.getRewardStatus())
            .createdAt(contract.getCreatedAt())
            .updatedAt(contract.getUpdatedAt())
            .options(contract.getContractOptions().stream()
                .map(ContractOptionResponseDto::fromEntity)
                .collect(Collectors.toList()))
            .build();
    }
}
