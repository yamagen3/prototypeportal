package com.prototypeportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 会員エンティティ
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_is_active", columnList = "is_active"),
    @Index(name = "idx_users_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Password hash is required")
    @Size(max = 255, message = "Password hash must not exceed 255 characters")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 100, message = "Name Kana must not exceed 100 characters")
    @Column(name = "name_kana", length = 100)
    private String nameKana;

    @Size(max = 255, message = "Company name must not exceed 255 characters")
    @Column(name = "company_name", length = 255)
    private String companyName;

    @Size(max = 100, message = "Department must not exceed 100 characters")
    @Column(length = 100)
    private String department;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(length = 20)
    private String phone;

    @Size(max = 10, message = "Postal code must not exceed 10 characters")
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(columnDefinition = "TEXT")
    private String address;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    /**
     * メールアドレスが確認済みかチェック
     */
    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    /**
     * アカウントがアクティブかチェック
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    /**
     * メールアドレスを確認済みにする
     */
    public void verifyEmail() {
        this.emailVerifiedAt = LocalDateTime.now();
    }

    /**
     * 最終ログイン日時を更新
     */
    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * アカウントを無効化
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * アカウントを有効化
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
