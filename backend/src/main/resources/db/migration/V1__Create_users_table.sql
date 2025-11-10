-- V1__Create_users_table.sql
-- 会員テーブルの作成

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    name_kana VARCHAR(100),
    company_name VARCHAR(255),
    department VARCHAR(100),
    phone VARCHAR(20),
    postal_code VARCHAR(10),
    address TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    email_verified_at TIMESTAMP
);

-- インデックスの作成
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_created_at ON users(created_at);

-- コメント追加
COMMENT ON TABLE users IS '会員テーブル';
COMMENT ON COLUMN users.id IS '会員ID（UUID）';
COMMENT ON COLUMN users.email IS 'メールアドレス（ログインID）';
COMMENT ON COLUMN users.password_hash IS 'パスワードハッシュ（BCrypt）';
COMMENT ON COLUMN users.name IS '氏名';
COMMENT ON COLUMN users.name_kana IS '氏名（カナ）';
COMMENT ON COLUMN users.company_name IS '会社名';
COMMENT ON COLUMN users.department IS '部署名';
COMMENT ON COLUMN users.phone IS '電話番号';
COMMENT ON COLUMN users.postal_code IS '郵便番号';
COMMENT ON COLUMN users.address IS '住所';
COMMENT ON COLUMN users.created_at IS '作成日時';
COMMENT ON COLUMN users.updated_at IS '更新日時';
COMMENT ON COLUMN users.last_login_at IS '最終ログイン日時';
COMMENT ON COLUMN users.is_active IS 'アクティブフラグ';
COMMENT ON COLUMN users.email_verified_at IS 'メールアドレス確認日時';
