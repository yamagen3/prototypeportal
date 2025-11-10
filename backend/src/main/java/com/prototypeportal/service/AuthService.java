package com.prototypeportal.service;

import com.prototypeportal.dto.AuthResponseDto;
import com.prototypeportal.dto.LoginDto;
import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.dto.UserResponseDto;
import com.prototypeportal.entity.User;
import com.prototypeportal.exception.InvalidCredentialsException;
import com.prototypeportal.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * 認証サービス
 */
@ApplicationScoped
@Transactional
public class AuthService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    /**
     * ユーザー登録
     */
    public AuthResponseDto register(UserRegistrationDto dto) {
        // UserServiceを使用してユーザー登録
        UserResponseDto userResponse = userService.register(dto);

        // JWTトークンを生成
        String accessToken = generateAccessToken(userResponse.getId());
        String refreshToken = generateRefreshToken(userResponse.getId());

        // レスポンスを構築
        return AuthResponseDto.builder()
            .user(userResponse)
            .tokens(AuthResponseDto.TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build())
            .build();
    }

    /**
     * ログイン
     */
    public AuthResponseDto login(LoginDto dto) {
        // メールアドレスでユーザーを検索
        User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // アクティブかチェック
        if (!user.isActive()) {
            throw new InvalidCredentialsException("Account is inactive");
        }

        // パスワード検証
        if (!verifyPassword(dto.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // 最終ログイン日時を更新
        user.updateLastLogin();
        userRepository.save(user);

        // JWTトークンを生成
        String accessToken = generateAccessToken(user.getId());
        String refreshToken = generateRefreshToken(user.getId());

        // レスポンスを構築
        return AuthResponseDto.builder()
            .user(UserResponseDto.fromEntity(user))
            .tokens(AuthResponseDto.TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build())
            .build();
    }

    /**
     * パスワード検証（仮実装）
     * TODO: BCryptを使用した検証に置き換える
     */
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        // 仮実装: プレフィックス付き比較
        return hashedPassword.equals("hashed_" + plainPassword);
    }

    /**
     * アクセストークン生成（仮実装）
     * TODO: JWT実装に置き換える
     */
    private String generateAccessToken(java.util.UUID userId) {
        return "access_token_" + userId.toString();
    }

    /**
     * リフレッシュトークン生成（仮実装）
     * TODO: JWT実装に置き換える
     */
    private String generateRefreshToken(java.util.UUID userId) {
        return "refresh_token_" + userId.toString();
    }
}
