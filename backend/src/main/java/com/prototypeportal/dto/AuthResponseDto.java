package com.prototypeportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認証レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private UserResponseDto user;
    private TokenDto tokens;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;
    }
}
