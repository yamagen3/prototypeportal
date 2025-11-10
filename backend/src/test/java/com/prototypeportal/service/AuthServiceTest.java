package com.prototypeportal.service;

import com.prototypeportal.dto.AuthResponseDto;
import com.prototypeportal.dto.LoginDto;
import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.entity.User;
import com.prototypeportal.exception.InvalidCredentialsException;
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
import static org.mockito.Mockito.*;

/**
 * AuthService のテスト
 *
 * TDD アプローチで認証ロジックをテスト
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService のテスト")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    private User existingUser;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .passwordHash("hashed_password123")
            .name("Test User")
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password123");
    }

    @Test
    @DisplayName("有効な認証情報でログインできる")
    void shouldLoginWithValidCredentials() {
        // Given
        when(userRepository.findByEmail(loginDto.getEmail()))
            .thenReturn(Optional.of(existingUser));

        // When
        AuthResponseDto result = authService.login(loginDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUser()).isNotNull();
        assertThat(result.getUser().getEmail()).isEqualTo(loginDto.getEmail());
        assertThat(result.getTokens()).isNotNull();
        assertThat(result.getTokens().getAccessToken()).isNotNull();

        verify(userRepository).findByEmail(loginDto.getEmail());
        verify(userRepository).save(any(User.class)); // lastLoginAt更新
    }

    @Test
    @DisplayName("存在しないメールアドレスでログインできない")
    void shouldThrowExceptionWhenEmailNotFound() {
        // Given
        when(userRepository.findByEmail(loginDto.getEmail()))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authService.login(loginDto))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessageContaining("Invalid email or password");

        verify(userRepository).findByEmail(loginDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("無効なパスワードでログインできない")
    void shouldThrowExceptionWhenPasswordInvalid() {
        // Given
        loginDto.setPassword("wrongpassword");
        when(userRepository.findByEmail(loginDto.getEmail()))
            .thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> authService.login(loginDto))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessageContaining("Invalid email or password");

        verify(userRepository).findByEmail(loginDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("非アクティブなユーザーはログインできない")
    void shouldThrowExceptionWhenUserIsInactive() {
        // Given
        existingUser.setIsActive(false);
        when(userRepository.findByEmail(loginDto.getEmail()))
            .thenReturn(Optional.of(existingUser));

        // When & Then
        assertThatThrownBy(() -> authService.login(loginDto))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessageContaining("Account is inactive");

        verify(userRepository).findByEmail(loginDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
}
