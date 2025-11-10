package com.prototypeportal.service;

import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.dto.UserResponseDto;
import com.prototypeportal.entity.User;
import com.prototypeportal.exception.EmailAlreadyExistsException;
import com.prototypeportal.exception.ResourceNotFoundException;
import com.prototypeportal.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ユーザーサービス
 */
@ApplicationScoped
@Transactional
public class UserService {

    @Inject
    private UserRepository userRepository;

    /**
     * ユーザー登録
     */
    public UserResponseDto register(UserRegistrationDto dto) {
        // メールアドレスの重複チェック
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        // Userエンティティの作成
        User user = User.builder()
            .email(dto.getEmail())
            .passwordHash(hashPassword(dto.getPassword())) // TODO: BCryptでハッシュ化
            .name(dto.getName())
            .nameKana(dto.getNameKana())
            .companyName(dto.getCompanyName())
            .department(dto.getDepartment())
            .phone(dto.getPhone())
            .postalCode(dto.getPostalCode())
            .address(dto.getAddress())
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // 保存
        User savedUser = userRepository.save(user);

        return UserResponseDto.fromEntity(savedUser);
    }

    /**
     * IDでユーザーを検索
     */
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        return UserResponseDto.fromEntity(user);
    }

    /**
     * メールアドレスでユーザーを検索
     */
    public Optional<UserResponseDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(UserResponseDto::fromEntity);
    }

    /**
     * アクティブなユーザー一覧を取得
     */
    public List<UserResponseDto> findAllActive() {
        return userRepository.findAllActive()
            .stream()
            .map(UserResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * ユーザーを更新
     */
    public UserResponseDto update(UUID id, UserRegistrationDto dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        // メールアドレスを変更する場合、重複チェック
        if (!user.getEmail().equals(dto.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }

        // 更新
        user.setName(dto.getName());
        user.setNameKana(dto.getNameKana());
        user.setCompanyName(dto.getCompanyName());
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhone());
        user.setPostalCode(dto.getPostalCode());
        user.setAddress(dto.getAddress());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        return UserResponseDto.fromEntity(updatedUser);
    }

    /**
     * ユーザーを無効化
     */
    public void deactivate(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        user.deactivate();
        userRepository.save(user);
    }

    /**
     * ユーザーを有効化
     */
    public void activate(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        user.activate();
        userRepository.save(user);
    }

    /**
     * パスワードをハッシュ化（仮実装）
     * TODO: BCryptを使用した実装に置き換える
     */
    private String hashPassword(String password) {
        // 仮実装: プレーンテキストをそのまま返す
        // 本番環境ではBCryptでハッシュ化すること
        return "hashed_" + password;
    }
}
