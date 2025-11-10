package com.prototypeportal.service;

import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.dto.UserResponseDto;
import com.prototypeportal.entity.User;
import com.prototypeportal.exception.EmailAlreadyExistsException;
import com.prototypeportal.exception.ResourceNotFoundException;
import com.prototypeportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UserService のテスト
 *
 * TDD アプローチ：
 * 1. テストを先に書く（Red）
 * 2. 実装を書く（Green）
 * 3. リファクタリング（Refactor）
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService のテスト")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto registrationDto;
    private User existingUser;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("newuser@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setName("New User");
        registrationDto.setCompanyName("Test Company");

        existingUser = User.builder()
            .id(UUID.randomUUID())
            .email("existing@example.com")
            .passwordHash("hashedPassword")
            .name("Existing User")
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("新規ユーザーを登録できる")
    void shouldRegisterNewUser() {
        // Given
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        // When
        UserResponseDto result = userService.register(registrationDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(registrationDto.getEmail());
        assertThat(result.getName()).isEqualTo(registrationDto.getName());
        assertThat(result.getCompanyName()).isEqualTo(registrationDto.getCompanyName());
        assertThat(result.getIsActive()).isTrue();

        verify(userRepository).existsByEmail(registrationDto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("既存のメールアドレスでは登録できない")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.register(registrationDto))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessageContaining("Email already registered");

        verify(userRepository).existsByEmail(registrationDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("IDでユーザーを取得できる")
    void shouldFindUserById() {
        // Given
        UUID userId = existingUser.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // When
        UserResponseDto result = userService.findById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(result.getName()).isEqualTo(existingUser.getName());

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("存在しないIDでユーザーを取得するとResourceNotFoundExceptionが発生する")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findById(userId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("User not found");

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("メールアドレスでユーザーを取得できる")
    void shouldFindUserByEmail() {
        // Given
        String email = existingUser.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When
        Optional<UserResponseDto> result = userService.findByEmail(email);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getName()).isEqualTo(existingUser.getName());

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("存在しないメールアドレスで取得するとOptional.emptyが返る")
    void shouldReturnEmptyWhenEmailNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDto> result = userService.findByEmail(email);

        // Then
        assertThat(result).isEmpty();

        verify(userRepository).findByEmail(email);
    }
}
