-- V2__Create_plans_and_options_tables.sql
-- プランテーブルとプランオプションテーブルの作成

-- プランテーブル
CREATE TABLE plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    plan_code VARCHAR(50) NOT NULL UNIQUE,
    plan_name VARCHAR(255) NOT NULL,
    description TEXT,
    base_price DECIMAL(10, 2),
    base_reward DECIMAL(10, 2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- プランオプションテーブル
CREATE TABLE plan_options (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    plan_id UUID NOT NULL,
    option_code VARCHAR(50) NOT NULL,
    option_name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    reward_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    is_required BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_plan_options_plan FOREIGN KEY (plan_id) REFERENCES plans(id) ON DELETE CASCADE
);

-- インデックスの作成
CREATE INDEX idx_plans_plan_code ON plans(plan_code);
CREATE INDEX idx_plans_is_active ON plans(is_active);
CREATE INDEX idx_plans_display_order ON plans(display_order);

CREATE INDEX idx_plan_options_plan_id ON plan_options(plan_id);
CREATE INDEX idx_plan_options_is_active ON plan_options(is_active);
CREATE INDEX idx_plan_options_display_order ON plan_options(display_order);

-- ユニーク制約（同一プラン内でのoption_codeの重複を防ぐ）
CREATE UNIQUE INDEX idx_plan_options_unique_code ON plan_options(plan_id, option_code);

-- コメント追加
COMMENT ON TABLE plans IS 'プラン・サービステーブル';
COMMENT ON COLUMN plans.id IS 'プランID（UUID）';
COMMENT ON COLUMN plans.plan_code IS 'プランコード';
COMMENT ON COLUMN plans.plan_name IS 'プラン名';
COMMENT ON COLUMN plans.description IS 'プラン説明';
COMMENT ON COLUMN plans.base_price IS '基本価格';
COMMENT ON COLUMN plans.base_reward IS '基本報酬額';
COMMENT ON COLUMN plans.is_active IS 'アクティブフラグ';
COMMENT ON COLUMN plans.display_order IS '表示順';
COMMENT ON COLUMN plans.created_at IS '作成日時';
COMMENT ON COLUMN plans.updated_at IS '更新日時';

COMMENT ON TABLE plan_options IS 'プランオプションテーブル';
COMMENT ON COLUMN plan_options.id IS 'オプションID（UUID）';
COMMENT ON COLUMN plan_options.plan_id IS 'プランID（外部キー）';
COMMENT ON COLUMN plan_options.option_code IS 'オプションコード';
COMMENT ON COLUMN plan_options.option_name IS 'オプション名';
COMMENT ON COLUMN plan_options.description IS 'オプション説明';
COMMENT ON COLUMN plan_options.price IS 'オプション価格';
COMMENT ON COLUMN plan_options.reward_amount IS 'オプション報酬額';
COMMENT ON COLUMN plan_options.is_required IS '必須フラグ';
COMMENT ON COLUMN plan_options.is_active IS 'アクティブフラグ';
COMMENT ON COLUMN plan_options.display_order IS '表示順';
COMMENT ON COLUMN plan_options.created_at IS '作成日時';
COMMENT ON COLUMN plan_options.updated_at IS '更新日時';
