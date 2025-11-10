package com.prototypeportal.service;

import com.prototypeportal.dto.PlanOptionResponseDto;
import com.prototypeportal.dto.PlanResponseDto;
import com.prototypeportal.entity.Plan;
import com.prototypeportal.entity.PlanOption;
import com.prototypeportal.exception.ResourceNotFoundException;
import com.prototypeportal.repository.PlanOptionRepository;
import com.prototypeportal.repository.PlanRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * プランサービス
 */
@ApplicationScoped
@Transactional
public class PlanService {

    @Inject
    private PlanRepository planRepository;

    @Inject
    private PlanOptionRepository planOptionRepository;

    /**
     * アクティブなプラン一覧を取得
     */
    public List<PlanResponseDto> findAllActivePlans() {
        return planRepository.findAllActive()
            .stream()
            .map(PlanResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * IDでプランを取得
     */
    public PlanResponseDto findById(UUID id) {
        Plan plan = planRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Plan", id.toString()));

        return PlanResponseDto.fromEntity(plan);
    }

    /**
     * プランコードでプランを取得
     */
    public PlanResponseDto findByPlanCode(String planCode) {
        Plan plan = planRepository.findByPlanCode(planCode)
            .orElseThrow(() -> new ResourceNotFoundException("Plan", planCode));

        return PlanResponseDto.fromEntity(plan);
    }

    /**
     * プランIDでアクティブなオプションを取得
     */
    public List<PlanOptionResponseDto> findActiveOptionsByPlanId(UUID planId) {
        // プランの存在確認
        planRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));

        return planOptionRepository.findByPlanIdAndActive(planId)
            .stream()
            .map(PlanOptionResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * オプションIDでオプションを取得
     */
    public PlanOptionResponseDto findOptionById(UUID optionId) {
        PlanOption option = planOptionRepository.findById(optionId)
            .orElseThrow(() -> new ResourceNotFoundException("PlanOption", optionId.toString()));

        return PlanOptionResponseDto.fromEntity(option);
    }
}
