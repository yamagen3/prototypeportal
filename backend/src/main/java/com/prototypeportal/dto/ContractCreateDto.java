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
 * 契約作成リクエストDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractCreateDto {

    @NotNull(message = "Plan ID is required")
    private UUID planId;

    @Builder.Default
    private List<ContractOptionCreateDto> options = new ArrayList<>();
}
