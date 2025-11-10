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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ContractServiceのテスト
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ContractService のテスト")
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractOptionRepository contractOptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanOptionRepository planOptionRepository;

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private ContractService contractService;

    private User user;
    private Plan plan;
    private PlanOption planOption1;
    private PlanOption planOption2;
    private UUID userId;
    private UUID planId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        planId = UUID.randomUUID();

        user = User.builder()
            .id(userId)
            .email("test@example.com")
            .name("Test User")
            .passwordHash("hashed_password")
            .build();

        plan = Plan.builder()
            .id(planId)
            .planCode("PLAN001")
            .planName("Standard Plan")
            .baseReward(BigDecimal.valueOf(1000))
            .basePrice(BigDecimal.valueOf(5000))
            .build();

        planOption1 = PlanOption.builder()
            .id(UUID.randomUUID())
            .plan(plan)
            .optionCode("OPT001")
            .optionName("Option 1")
            .price(BigDecimal.valueOf(1000))
            .rewardAmount(BigDecimal.valueOf(500))
            .build();

        planOption2 = PlanOption.builder()
            .id(UUID.randomUUID())
            .plan(plan)
            .optionCode("OPT002")
            .optionName("Option 2")
            .price(BigDecimal.valueOf(2000))
            .rewardAmount(BigDecimal.valueOf(800))
            .build();
    }

    @Test
    @DisplayName("新規契約を作成できる（オプションなし）")
    void shouldCreateNewContractWithoutOptions() {
        ContractCreateDto dto = ContractCreateDto.builder()
            .planId(planId)
            .options(new ArrayList<>())
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(rewardService.calculateTotalReward(eq(plan), any())).thenReturn(BigDecimal.valueOf(1000));
        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract contract = invocation.getArgument(0);
            contract.setId(UUID.randomUUID());
            return contract;
        });

        ContractResponseDto result = contractService.createContract(userId, dto);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getPlanId()).isEqualTo(planId);
        assertThat(result.getStatus()).isEqualTo(ContractStatus.DRAFT);
        assertThat(result.getRewardStatus()).isEqualTo(RewardStatus.PENDING);
        assertThat(result.getApplicationNumber()).isNotNull();

        verify(contractRepository).save(any(Contract.class));
        verify(rewardService).calculateTotalReward(eq(plan), any());
    }

    @Test
    @DisplayName("新規契約を作成できる（オプションあり）")
    void shouldCreateNewContractWithOptions() {
        ContractOptionCreateDto optionDto1 = ContractOptionCreateDto.builder()
            .planOptionId(planOption1.getId())
            .quantity(2)
            .build();

        ContractCreateDto dto = ContractCreateDto.builder()
            .planId(planId)
            .options(List.of(optionDto1))
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(planOptionRepository.findById(planOption1.getId())).thenReturn(Optional.of(planOption1));
        when(rewardService.calculateTotalReward(eq(plan), any())).thenReturn(BigDecimal.valueOf(2000));
        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract contract = invocation.getArgument(0);
            contract.setId(UUID.randomUUID());
            return contract;
        });

        ContractResponseDto result = contractService.createContract(userId, dto);

        assertThat(result).isNotNull();
        assertThat(result.getOptions()).hasSize(1);
        assertThat(result.getTotalRewardAmount()).isEqualByComparingTo(BigDecimal.valueOf(2000));

        ArgumentCaptor<Contract> contractCaptor = ArgumentCaptor.forClass(Contract.class);
        verify(contractRepository).save(contractCaptor.capture());

        Contract savedContract = contractCaptor.getValue();
        assertThat(savedContract.getContractOptions()).hasSize(1);
    }

    @Test
    @DisplayName("存在しないユーザーで契約を作成すると例外を投げる")
    void shouldThrowExceptionWhenUserNotFound() {
        ContractCreateDto dto = ContractCreateDto.builder()
            .planId(planId)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractService.createContract(userId, dto))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("存在しないプランで契約を作成すると例外を投げる")
    void shouldThrowExceptionWhenPlanNotFound() {
        ContractCreateDto dto = ContractCreateDto.builder()
            .planId(planId)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractService.createContract(userId, dto))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("IDで契約を取得できる")
    void shouldFindContractById() {
        UUID contractId = UUID.randomUUID();
        Contract contract = Contract.builder()
            .id(contractId)
            .user(user)
            .plan(plan)
            .applicationNumber("APP-20250101-001")
            .status(ContractStatus.DRAFT)
            .rewardStatus(RewardStatus.PENDING)
            .totalRewardAmount(BigDecimal.valueOf(1000))
            .contractOptions(new ArrayList<>())
            .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        ContractResponseDto result = contractService.findById(contractId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(contractId);
        assertThat(result.getApplicationNumber()).isEqualTo("APP-20250101-001");
    }

    @Test
    @DisplayName("存在しないIDで契約を取得すると例外を投げる")
    void shouldThrowExceptionWhenContractNotFoundById() {
        UUID contractId = UUID.randomUUID();

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractService.findById(contractId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Contract");
    }

    @Test
    @DisplayName("ユーザーIDで契約一覧を取得できる")
    void shouldFindContractsByUserId() {
        Contract contract1 = Contract.builder()
            .id(UUID.randomUUID())
            .user(user)
            .plan(plan)
            .applicationNumber("APP-001")
            .status(ContractStatus.DRAFT)
            .contractOptions(new ArrayList<>())
            .build();

        Contract contract2 = Contract.builder()
            .id(UUID.randomUUID())
            .user(user)
            .plan(plan)
            .applicationNumber("APP-002")
            .status(ContractStatus.SUBMITTED)
            .contractOptions(new ArrayList<>())
            .build();

        when(contractRepository.findByUserId(userId)).thenReturn(List.of(contract1, contract2));

        List<ContractResponseDto> result = contractService.findByUserId(userId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getApplicationNumber()).isEqualTo("APP-001");
        assertThat(result.get(1).getApplicationNumber()).isEqualTo("APP-002");
    }

    @Test
    @DisplayName("契約を申し込める")
    void shouldSubmitContract() {
        UUID contractId = UUID.randomUUID();
        Contract contract = Contract.builder()
            .id(contractId)
            .user(user)
            .plan(plan)
            .applicationNumber("APP-20250101-001")
            .status(ContractStatus.DRAFT)
            .rewardStatus(RewardStatus.PENDING)
            .totalRewardAmount(BigDecimal.valueOf(1000))
            .contractOptions(new ArrayList<>())
            .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        ContractResponseDto result = contractService.submitContract(contractId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(ContractStatus.SUBMITTED);
        assertThat(result.getSubmittedAt()).isNotNull();

        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    @DisplayName("申込番号を生成できる")
    void shouldGenerateApplicationNumber() {
        String appNumber = contractService.generateApplicationNumber();

        assertThat(appNumber).isNotNull();
        assertThat(appNumber).startsWith("APP-");
        assertThat(appNumber).hasSize(24); // APP- + 8桁日付 + - + 12桁ランダム = 24文字
    }
}
