package com.prototypeportal.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API レスポンスラッパー
 *
 * 標準的なレスポンス形式を提供
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private T data;
    private String message;
    private String errorCode;

    /**
     * 成功レスポンスを作成
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .data(data)
            .build();
    }

    /**
     * 成功レスポンス（メッセージ付き）を作成
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .data(data)
            .message(message)
            .build();
    }

    /**
     * エラーレスポンスを作成
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
            .message(message)
            .errorCode(errorCode)
            .build();
    }
}
