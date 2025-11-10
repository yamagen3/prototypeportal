package com.prototypeportal.service;

import com.prototypeportal.dto.ContractCreateDto;
import com.prototypeportal.dto.ContractOptionCreateDto;
import com.prototypeportal.dto.ContractResponseDto;
import com.prototypeportal.entity.*;
import com.prototypeportal.exception.ResourceNotFoundException;
import com.prototypeportal.repository.ContractOptionRepository;
import com.prototypeportal.repository.ContractRepository;
import com.prototypeportal.repository.PlanOptionRepository;
import com.prototypeportal.repository.PlanRepository;
import com.prototypeportal.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 契約サービス
 */
@ApplicationScoped
@Transactional
public class ContractService {

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private ContractOptionRepository contractOptionRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlanRepository planRepository;

    @Inject
    private PlanOptionRepository planOptionRepository;

    @Inject
    private RewardService rewardService;

    private static final Random RANDOM = new Random();

    /**
     * 新規契約を作成
     */
    public ContractResponseDto createContract(UUID userId, ContractCreateDto dto) {
        // ユーザーとプランの存在確認
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        Plan plan = planRepository.findById(dto.getPlanId())
            .orElseThrow(() -> new ResourceNotFoundException("Plan", dto.getPlanId().toString()));

        // 契約エンティティを作成
        Contract contract = Contract.builder()
            .user(user)
            .plan(plan)
            .applicationNumber(generateApplicationNumber())
            .status(ContractStatus.DRAFT)
            .rewardStatus(RewardStatus.PENDING)
            .build();

        // オプションを追加
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            for (ContractOptionCreateDto optionDto : dto.getOptions()) {
                PlanOption planOption = planOptionRepository.findById(optionDto.getPlanOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("PlanOption", optionDto.getPlanOptionId().toString()));

                ContractOption contractOption = ContractOption.builder()
                    .planOption(planOption)
                    .quantity(optionDto.getQuantity())
                    .price(planOption.getPrice())
                    .rewardAmount(planOption.getRewardAmount())
                    .build();

                contract.addContractOption(contractOption);
            }
        }

        // 合計報酬額を計算
        contract.calculateTotalRewardAmount(plan.getBaseReward());

        // 保存
        Contract savedContract = contractRepository.save(contract);

        return ContractResponseDto.fromEntity(savedContract);
    }

    /**
     * IDで契約を取得
     */
    public ContractResponseDto findById(UUID id) {
        Contract contract = contractRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contract", id.toString()));

        return ContractResponseDto.fromEntity(contract);
    }

    /**
     * ユーザーIDで契約一覧を取得
     */
    public List<ContractResponseDto> findByUserId(UUID userId) {
        return contractRepository.findByUserId(userId)
            .stream()
            .map(ContractResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * ユーザーIDとステータスで契約一覧を取得
     */
    public List<ContractResponseDto> findByUserIdAndStatus(UUID userId, ContractStatus status) {
        return contractRepository.findByUserIdAndStatus(userId, status)
            .stream()
            .map(ContractResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * 契約を申し込む
     */
    public ContractResponseDto submitContract(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract", contractId.toString()));

        contract.submit();
        Contract savedContract = contractRepository.save(contract);

        return ContractResponseDto.fromEntity(savedContract);
    }

    /**
     * 契約を更新
     */
    public ContractResponseDto updateContract(UUID contractId, ContractCreateDto dto) {
        Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract", contractId.toString()));

        // 下書き状態のみ更新可能
        if (!contract.isDraft()) {
            throw new IllegalStateException("Only draft contracts can be updated");
        }

        // プランの更新
        if (!contract.getPlan().getId().equals(dto.getPlanId())) {
            Plan newPlan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan", dto.getPlanId().toString()));
            contract.setPlan(newPlan);
        }

        // 既存のオプションをクリア
        contract.getContractOptions().clear();

        // 新しいオプションを追加
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            for (ContractOptionCreateDto optionDto : dto.getOptions()) {
                PlanOption planOption = planOptionRepository.findById(optionDto.getPlanOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("PlanOption", optionDto.getPlanOptionId().toString()));

                ContractOption contractOption = ContractOption.builder()
                    .planOption(planOption)
                    .quantity(optionDto.getQuantity())
                    .price(planOption.getPrice())
                    .rewardAmount(planOption.getRewardAmount())
                    .build();

                contract.addContractOption(contractOption);
            }
        }

        // 合計報酬額を再計算
        contract.calculateTotalRewardAmount(contract.getPlan().getBaseReward());

        Contract savedContract = contractRepository.save(contract);

        return ContractResponseDto.fromEntity(savedContract);
    }

    /**
     * 申込番号を生成
     * フォーマット: APP-YYYYMMDD-XXXXXXXXXXXX (XXは12桁のランダム数字)
     */
    public String generateApplicationNumber() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%012d", RANDOM.nextLong(1_000_000_000_000L));
        return "APP-" + dateStr + "-" + randomStr;
    }
}
