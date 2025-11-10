package com.prototypeportal.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StringUtils のテストクラス
 *
 * TDD アプローチ：
 * 1. テストを先に書く（Red）
 * 2. 最小限の実装で通す（Green）
 * 3. リファクタリング（Refactor）
 */
@DisplayName("StringUtils のテスト")
class StringUtilsTest {

    @Nested
    @DisplayName("isEmpty メソッドのテスト")
    class IsEmptyTest {

        @Test
        @DisplayName("null の文字列は空と判定される")
        void shouldReturnTrueForNull() {
            assertThat(StringUtils.isEmpty(null)).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("空文字列または空白文字のみの文字列は空と判定される")
        void shouldReturnTrueForEmptyOrBlank(String input) {
            assertThat(StringUtils.isEmpty(input)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "test", " test ", "123"})
        @DisplayName("文字を含む文字列は空でないと判定される")
        void shouldReturnFalseForNonEmpty(String input) {
            assertThat(StringUtils.isEmpty(input)).isFalse();
        }
    }

    @Nested
    @DisplayName("isNotEmpty メソッドのテスト")
    class IsNotEmptyTest {

        @Test
        @DisplayName("null の文字列は空でないとは判定されない")
        void shouldReturnFalseForNull() {
            assertThat(StringUtils.isNotEmpty(null)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "test", "123"})
        @DisplayName("文字を含む文字列は空でないと判定される")
        void shouldReturnTrueForNonEmpty(String input) {
            assertThat(StringUtils.isNotEmpty(input)).isTrue();
        }
    }

    @Nested
    @DisplayName("isValidEmail メソッドのテスト")
    class IsValidEmailTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "test@example.com",
                "user.name@example.com",
                "user+tag@example.co.jp",
                "123@test.com"
        })
        @DisplayName("有効なメールアドレス形式の場合trueを返す")
        void shouldReturnTrueForValidEmail(String email) {
            assertThat(StringUtils.isValidEmail(email)).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
                "invalid",
                "@example.com",
                "user@",
                "user @example.com",
                "user@.com",
                "user..name@example.com"
        })
        @DisplayName("無効なメールアドレス形式の場合falseを返す")
        void shouldReturnFalseForInvalidEmail(String email) {
            assertThat(StringUtils.isValidEmail(email)).isFalse();
        }
    }

    @Nested
    @DisplayName("isStrongPassword メソッドのテスト")
    class IsStrongPasswordTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "password123",
                "Test1234",
                "abcd1234efgh",
                "Secure99"
        })
        @DisplayName("8文字以上で英字と数字を含むパスワードはtrueを返す")
        void shouldReturnTrueForStrongPassword(String password) {
            assertThat(StringUtils.isStrongPassword(password)).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
                "short1",          // 8文字未満
                "password",        // 数字なし
                "12345678",        // 英字なし
                "   test123   "    // 8文字未満（トリム後）
        })
        @DisplayName("弱いパスワードはfalseを返す")
        void shouldReturnFalseForWeakPassword(String password) {
            assertThat(StringUtils.isStrongPassword(password)).isFalse();
        }

        @Test
        @DisplayName("null のパスワードはfalseを返す")
        void shouldReturnFalseForNull() {
            assertThat(StringUtils.isStrongPassword(null)).isFalse();
        }
    }
}
