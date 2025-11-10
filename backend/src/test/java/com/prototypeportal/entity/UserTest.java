package com.prototypeportal.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User エンティティのテスト
 *
 * TDDアプローチ：
 * エンティティのビジネスロジックメソッドをテスト
 */
@DisplayName("User エンティティのテスト")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .name("Test User")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("メールアドレス確認のテスト")
    class EmailVerificationTest {

        @Test
        @DisplayName("メールアドレスが未確認の場合、isEmailVerifiedはfalseを返す")
        void shouldReturnFalseWhenEmailNotVerified() {
            user.setEmailVerifiedAt(null);

            assertThat(user.isEmailVerified()).isFalse();
        }

        @Test
        @DisplayName("メールアドレスが確認済みの場合、isEmailVerifiedはtrueを返す")
        void shouldReturnTrueWhenEmailVerified() {
            user.setEmailVerifiedAt(LocalDateTime.now());

            assertThat(user.isEmailVerified()).isTrue();
        }

        @Test
        @DisplayName("verifyEmailを呼ぶと、emailVerifiedAtが設定される")
        void shouldSetEmailVerifiedAtWhenVerifyEmailCalled() {
            user.setEmailVerifiedAt(null);
            LocalDateTime before = LocalDateTime.now();

            user.verifyEmail();

            assertThat(user.getEmailVerifiedAt()).isNotNull();
            assertThat(user.getEmailVerifiedAt()).isAfterOrEqualTo(before);
            assertThat(user.isEmailVerified()).isTrue();
        }
    }

    @Nested
    @DisplayName("アカウントアクティブ状態のテスト")
    class AccountActiveTest {

        @Test
        @DisplayName("isActiveがtrueの場合、アクティブと判定される")
        void shouldReturnTrueWhenIsActiveTrue() {
            user.setIsActive(true);

            assertThat(user.isActive()).isTrue();
        }

        @Test
        @DisplayName("isActiveがfalseの場合、非アクティブと判定される")
        void shouldReturnFalseWhenIsActiveFalse() {
            user.setIsActive(false);

            assertThat(user.isActive()).isFalse();
        }

        @Test
        @DisplayName("isActiveがnullの場合、非アクティブと判定される")
        void shouldReturnFalseWhenIsActiveNull() {
            user.setIsActive(null);

            assertThat(user.isActive()).isFalse();
        }

        @Test
        @DisplayName("deactivateを呼ぶと、isActiveがfalseになる")
        void shouldSetIsActiveFalseWhenDeactivateCalled() {
            user.setIsActive(true);
            LocalDateTime beforeUpdate = user.getUpdatedAt();

            // 時間の経過を待つ（ミリ秒単位の差を確保するため）
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            user.deactivate();

            assertThat(user.getIsActive()).isFalse();
            assertThat(user.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("activateを呼ぶと、isActiveがtrueになる")
        void shouldSetIsActiveTrueWhenActivateCalled() {
            user.setIsActive(false);
            LocalDateTime beforeUpdate = user.getUpdatedAt();

            // 時間の経過を待つ
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            user.activate();

            assertThat(user.getIsActive()).isTrue();
            assertThat(user.getUpdatedAt()).isAfter(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("最終ログイン日時のテスト")
    class LastLoginTest {

        @Test
        @DisplayName("updateLastLoginを呼ぶと、lastLoginAtが更新される")
        void shouldUpdateLastLoginAtWhenUpdateLastLoginCalled() {
            user.setLastLoginAt(null);
            LocalDateTime before = LocalDateTime.now();

            user.updateLastLogin();

            assertThat(user.getLastLoginAt()).isNotNull();
            assertThat(user.getLastLoginAt()).isAfterOrEqualTo(before);
        }

        @Test
        @DisplayName("updateLastLoginを複数回呼ぶと、最新の日時に更新される")
        void shouldUpdateToLatestWhenUpdateLastLoginCalledMultipleTimes() {
            user.updateLastLogin();
            LocalDateTime firstLogin = user.getLastLoginAt();

            // 時間の経過を待つ
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            user.updateLastLogin();
            LocalDateTime secondLogin = user.getLastLoginAt();

            assertThat(secondLogin).isAfter(firstLogin);
        }
    }

    @Nested
    @DisplayName("Builderパターンのテスト")
    class BuilderTest {

        @Test
        @DisplayName("Builderで必須フィールドを設定してUserを作成できる")
        void shouldCreateUserWithRequiredFields() {
            User newUser = User.builder()
                    .email("newuser@example.com")
                    .passwordHash("hashedPassword123")
                    .name("New User")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            assertThat(newUser.getEmail()).isEqualTo("newuser@example.com");
            assertThat(newUser.getPasswordHash()).isEqualTo("hashedPassword123");
            assertThat(newUser.getName()).isEqualTo("New User");
            assertThat(newUser.getIsActive()).isTrue(); // デフォルト値
        }

        @Test
        @DisplayName("Builderでオプショナルフィールドも設定できる")
        void shouldCreateUserWithOptionalFields() {
            User newUser = User.builder()
                    .email("newuser@example.com")
                    .passwordHash("hashedPassword123")
                    .name("New User")
                    .nameKana("ニューユーザー")
                    .companyName("Test Company")
                    .department("IT Department")
                    .phone("090-1234-5678")
                    .postalCode("100-0001")
                    .address("Tokyo")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            assertThat(newUser.getNameKana()).isEqualTo("ニューユーザー");
            assertThat(newUser.getCompanyName()).isEqualTo("Test Company");
            assertThat(newUser.getDepartment()).isEqualTo("IT Department");
            assertThat(newUser.getPhone()).isEqualTo("090-1234-5678");
            assertThat(newUser.getPostalCode()).isEqualTo("100-0001");
            assertThat(newUser.getAddress()).isEqualTo("Tokyo");
        }
    }
}
