-- V4__Create_rewards_notifications_logs_tables.sql
-- 報酬、通知、外部システム連携ログテーブルの作成

-- 報酬タイプのENUM型
CREATE TYPE reward_type AS ENUM (
    'contract',
    'bonus',
    'adjustment'
);

-- 通知タイプのENUM型
CREATE TYPE notification_type AS ENUM (
    'contract_submitted',
    'contract_approved',
    'contract_rejected',
    'reward_confirmed',
    'reward_paid',
    'system'
);

-- 外部連携タイプのENUM型
CREATE TYPE sync_type AS ENUM (
    'submit',
    'status_update',
    'callback'
);

-- 同期ステータスのENUM型
CREATE TYPE sync_status AS ENUM (
    'success',
    'error',
    'retry'
);

-- 報酬履歴テーブル
CREATE TABLE rewards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    contract_id UUID NOT NULL,
    reward_type reward_type NOT NULL DEFAULT 'contract',
    amount DECIMAL(10, 2) NOT NULL,
    status reward_status NOT NULL DEFAULT 'pending',
    confirmed_at TIMESTAMP,
    paid_at TIMESTAMP,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rewards_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_rewards_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE,
    CONSTRAINT chk_rewards_amount CHECK (amount >= 0)
);

-- 通知テーブル
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    type notification_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 外部システム連携ログテーブル
CREATE TABLE external_sync_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    contract_id UUID NOT NULL,
    sync_type sync_type NOT NULL,
    request_payload JSONB,
    response_payload JSONB,
    status sync_status NOT NULL DEFAULT 'success',
    error_message TEXT,
    retry_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_external_sync_logs_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE,
    CONSTRAINT chk_external_sync_logs_retry_count CHECK (retry_count >= 0)
);

-- インデックスの作成
CREATE INDEX idx_rewards_user_id ON rewards(user_id);
CREATE INDEX idx_rewards_contract_id ON rewards(contract_id);
CREATE INDEX idx_rewards_status ON rewards(status);
CREATE INDEX idx_rewards_created_at ON rewards(created_at);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

CREATE INDEX idx_external_sync_logs_contract_id ON external_sync_logs(contract_id);
CREATE INDEX idx_external_sync_logs_status ON external_sync_logs(status);
CREATE INDEX idx_external_sync_logs_created_at ON external_sync_logs(created_at);

-- コメント追加
COMMENT ON TABLE rewards IS '報酬履歴テーブル';
COMMENT ON COLUMN rewards.id IS '報酬ID（UUID）';
COMMENT ON COLUMN rewards.user_id IS '会員ID（外部キー）';
COMMENT ON COLUMN rewards.contract_id IS '契約ID（外部キー）';
COMMENT ON COLUMN rewards.reward_type IS '報酬タイプ';
COMMENT ON COLUMN rewards.amount IS '報酬額';
COMMENT ON COLUMN rewards.status IS 'ステータス';
COMMENT ON COLUMN rewards.confirmed_at IS '確定日時';
COMMENT ON COLUMN rewards.paid_at IS '支払日時';
COMMENT ON COLUMN rewards.description IS '説明';
COMMENT ON COLUMN rewards.created_at IS '作成日時';
COMMENT ON COLUMN rewards.updated_at IS '更新日時';

COMMENT ON TABLE notifications IS '通知テーブル';
COMMENT ON COLUMN notifications.id IS '通知ID（UUID）';
COMMENT ON COLUMN notifications.user_id IS '会員ID（外部キー）';
COMMENT ON COLUMN notifications.type IS '通知タイプ';
COMMENT ON COLUMN notifications.title IS 'タイトル';
COMMENT ON COLUMN notifications.message IS 'メッセージ';
COMMENT ON COLUMN notifications.is_read IS '既読フラグ';
COMMENT ON COLUMN notifications.read_at IS '既読日時';
COMMENT ON COLUMN notifications.created_at IS '作成日時';

COMMENT ON TABLE external_sync_logs IS '外部システム連携ログテーブル';
COMMENT ON COLUMN external_sync_logs.id IS 'ログID（UUID）';
COMMENT ON COLUMN external_sync_logs.contract_id IS '契約ID（外部キー）';
COMMENT ON COLUMN external_sync_logs.sync_type IS '連携タイプ';
COMMENT ON COLUMN external_sync_logs.request_payload IS 'リクエストペイロード（JSON）';
COMMENT ON COLUMN external_sync_logs.response_payload IS 'レスポンスペイロード（JSON）';
COMMENT ON COLUMN external_sync_logs.status IS 'ステータス';
COMMENT ON COLUMN external_sync_logs.error_message IS 'エラーメッセージ';
COMMENT ON COLUMN external_sync_logs.retry_count IS 'リトライ回数';
COMMENT ON COLUMN external_sync_logs.created_at IS '作成日時';
