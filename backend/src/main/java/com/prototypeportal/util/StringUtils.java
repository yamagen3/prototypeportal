package com.prototypeportal.util;

/**
 * 文字列ユーティリティクラス
 */
public class StringUtils {

    /**
     * 文字列が空またはnullかチェック
     *
     * @param str チェック対象の文字列
     * @return 空またはnullの場合true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 文字列が空でないかチェック
     *
     * @param str チェック対象の文字列
     * @return 空でない場合true
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * メールアドレス形式の簡易バリデーション
     *
     * @param email チェック対象のメールアドレス
     * @return 有効なメールアドレス形式の場合true
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }

        // 簡易的なメールアドレスパターンチェック
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * パスワードの強度チェック
     * - 最低8文字
     * - 英字と数字を含む
     *
     * @param password チェック対象のパスワード
     * @return 強度要件を満たす場合true
     */
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password) || password.length() < 8) {
            return false;
        }

        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasLetter && hasDigit;
    }
}
