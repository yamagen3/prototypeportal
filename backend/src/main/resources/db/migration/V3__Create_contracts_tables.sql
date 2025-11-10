-- V3__Create_contracts_tables.sql
-- 契約テーブルと契約オプションテーブルの作成

-- 契約ステータスのENUM型
CREATE TYPE contract_status AS ENUM (
    'draft',
    'pending',
    'submitted',
    'approved',
    'rejected',
    'active',
    'cancelled'
);

-- 報酬ステータスのENUM型
CREATE TYPE reward_status AS ENUM (
    'pending',
    'confirmed',
    'paid'
);

-- 契約テーブル
CREATE TABLE contracts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    application_number VARCHAR(50) NOT NULL UNIQUE,
    status contract_status NOT NULL DEFAULT 'draft',
    submitted_at TIMESTAMP,
    approved_at TIMESTAMP,
    rejected_at TIMESTAMP,
    rejection_reason TEXT,
    external_approval_id VARCHAR(100),
    contract_start_date DATE,
    contract_end_date DATE,
    total_reward_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    reward_status reward_status NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contracts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_contracts_plan FOREIGN KEY (plan_id) REFERENCES plans(id) ON DELETE RESTRICT
);

-- 契約オプション関連テーブル
CREATE TABLE contract_options (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    contract_id UUID NOT NULL,
    plan_option_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(10, 2) NOT NULL,
    reward_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contract_options_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE,
    CONSTRAINT fk_contract_options_plan_option FOREIGN KEY (plan_option_id) REFERENCES plan_options(id) ON DELETE RESTRICT,
    CONSTRAINT chk_contract_options_quantity CHECK (quantity > 0)
);

-- インデックスの作成
CREATE INDEX idx_contracts_user_id ON contracts(user_id);
CREATE INDEX idx_contracts_plan_id ON contracts(plan_id);
CREATE INDEX idx_contracts_status ON contracts(status);
CREATE INDEX idx_contracts_reward_status ON contracts(reward_status);
CREATE INDEX idx_contracts_application_number ON contracts(application_number);
CREATE INDEX idx_contracts_external_approval_id ON contracts(external_approval_id);
CREATE INDEX idx_contracts_created_at ON contracts(created_at);

CREATE INDEX idx_contract_options_contract_id ON contract_options(contract_id);
CREATE INDEX idx_contract_options_plan_option_id ON contract_options(plan_option_id);

-- コメント追加
COMMENT ON TABLE contracts IS '契約テーブル';
COMMENT ON COLUMN contracts.id IS '契約ID（UUID）';
COMMENT ON COLUMN contracts.user_id IS '会員ID（外部キー）';
COMMENT ON COLUMN contracts.plan_id IS 'プランID（外部キー）';
COMMENT ON COLUMN contracts.application_number IS '申込番号';
COMMENT ON COLUMN contracts.status IS '契約ステータス';
COMMENT ON COLUMN contracts.submitted_at IS '申込日時';
COMMENT ON COLUMN contracts.approved_at IS '承認日時';
COMMENT ON COLUMN contracts.rejected_at IS '却下日時';
COMMENT ON COLUMN contracts.rejection_reason IS '却下理由';
COMMENT ON COLUMN contracts.external_approval_id IS '外部承認システムID';
COMMENT ON COLUMN contracts.contract_start_date IS '契約開始日';
COMMENT ON COLUMN contracts.contract_end_date IS '契約終了日';
COMMENT ON COLUMN contracts.total_reward_amount IS '合計報酬額';
COMMENT ON COLUMN contracts.reward_status IS '報酬ステータス';
COMMENT ON COLUMN contracts.created_at IS '作成日時';
COMMENT ON COLUMN contracts.updated_at IS '更新日時';

COMMENT ON TABLE contract_options IS '契約オプション関連テーブル';
COMMENT ON COLUMN contract_options.id IS 'ID（UUID）';
COMMENT ON COLUMN contract_options.contract_id IS '契約ID（外部キー）';
COMMENT ON COLUMN contract_options.plan_option_id IS 'プランオプションID（外部キー）';
COMMENT ON COLUMN contract_options.quantity IS '数量';
COMMENT ON COLUMN contract_options.price IS '価格（スナップショット）';
COMMENT ON COLUMN contract_options.reward_amount IS '報酬額（スナップショット）';
COMMENT ON COLUMN contract_options.created_at IS '作成日時';
