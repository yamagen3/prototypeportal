package com.prototypeportal.dto;

import com.prototypeportal.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ユーザー情報レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private UUID id;
    private String email;
    private String name;
    private String nameKana;
    private String companyName;
    private String department;
    private String phone;
    private String postalCode;
    private String address;
    private Boolean isActive;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    /**
     * UserエンティティからDTOに変換
     */
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .nameKana(user.getNameKana())
            .companyName(user.getCompanyName())
            .department(user.getDepartment())
            .phone(user.getPhone())
            .postalCode(user.getPostalCode())
            .address(user.getAddress())
            .isActive(user.getIsActive())
            .emailVerified(user.isEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }
}
