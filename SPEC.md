# ポータルアプリケーション仕様書

## 1. 目的（Purpose/Objective）

### 1.1 概要
このポータルアプリケーションは、既存のサービス会員が新たな契約を行う際に使用するプラットフォームである。

### 1.2 主な目的
- 既存会員に対して、スムーズな追加契約体験を提供する
- 契約プロセスを簡潔化し、ユーザーの離脱を防ぐ
- 会員情報を活用して、入力の手間を削減する
- 契約に伴う報酬情報を透明に提示し、会員のモチベーションを向上させる
- 外部承認システムとシームレスに連携し、一貫した契約フローを実現する

### 1.3 対象ユーザー
- **プライマリユーザー**: 既存サービスの会員（ログイン済み）
- **ユーザー特性**: プロフェッショナル層（B2B/B2B2P）
  - 専門知識を持つビジネスユーザー
  - 効率性と正確性を重視
  - 詳細な情報と高度な機能を求める
- **利用シーン**: 追加契約・新規サービス申し込み時

### 1.4 解決する課題
- 既存会員が新規契約する際の情報再入力の手間
- 契約プロセスの複雑さによる離脱率の高さ
- 会員情報と契約情報の不整合
- プロフェッショナルユーザーの業務効率化ニーズへの対応
- ビジネス要件に応じた柔軟な契約オプションの提供

---

## 2. 要件（Requirements）

### 2.1 機能要件（Functional Requirements）

#### 2.1.1 会員登録機能
- **新規会員登録**
  - 必要情報の入力フォーム
  - メールアドレス確認
  - パスワード設定
  - 会員情報の登録（氏名、会社情報、連絡先など）
  - 利用規約・プライバシーポリシーへの同意
- **入力バリデーション**
  - リアルタイム入力チェック
  - エラーメッセージの表示
- **登録完了通知**
  - 確認メールの送信

#### 2.1.2 ログイン機能
- **認証方式**
  - メールアドレス + パスワード認証
  - セッション管理
  - ログイン状態の保持
- **セキュリティ機能**
  - パスワード再設定機能
  - ログイン試行回数制限
  - セッションタイムアウト
- **その他**
  - ログイン状態の記憶（Remember Me）
  - ログアウト機能

#### 2.1.3 メインメニュー
- **ダッシュボード**
  - 会員情報サマリー表示
  - 現在の契約状況一覧
  - **報酬サマリー表示（累計報酬、今月の報酬など）**
  - **申し込みステータス一覧（承認待ち件数など）**
  - 重要なお知らせ・通知
- **ナビゲーション**
  - 契約申し込みへの導線
  - 契約履歴の参照
  - **報酬履歴の参照**
  - **申し込みステータス確認**
  - 会員情報の編集
  - ヘルプ・サポート
- **アカウント管理**
  - プロフィール編集
  - パスワード変更

#### 2.1.4 契約申し込み機能
- **プラン・サービス選択**
  - 利用可能なプラン一覧表示
  - プラン詳細情報の確認
  - プラン比較機能
  - **各プラン・オプションに紐づく報酬情報の表示**
- **申し込みフォーム**
  - 会員情報の自動入力（既存会員情報活用）
  - 追加情報の入力
  - オプション選択
  - **選択内容に応じた報酬額のリアルタイム計算・表示**
- **確認・送信**
  - 入力内容の確認画面
  - 利用規約の確認
  - **予想報酬額の最終確認**
  - 申し込み内容の送信
  - **外部承認システムへのデータ連携**
- **完了・通知**
  - 申し込み完了画面
  - 確認メールの送信
  - 申し込み番号の発行
  - 承認待ちステータスの表示
  - **外部システムでの承認プロセスへの案内**

#### 2.1.5 報酬管理機能
- **報酬情報表示**
  - 契約に伴う報酬の詳細表示
  - 報酬計算ロジックの説明
  - 報酬受取条件の明示
- **報酬履歴**
  - 過去の報酬履歴一覧
  - 契約ごとの報酬内訳
  - 報酬ステータス（確定・未確定・支払済など）
- **報酬シミュレーション**
  - プラン選択前の報酬シミュレーション機能
  - 複数パターンの比較

#### 2.1.6 契約ステータス管理機能
- **申し込み状況確認**
  - 申し込み一覧の表示
  - 各申し込みのステータス確認（申請中・承認待ち・承認済・却下）
  - **外部承認システムからのステータス同期**
- **承認結果通知**
  - 承認完了時の通知
  - 却下時の理由表示
- **契約履歴**
  - 承認済み契約の履歴表示
  - 契約詳細情報の閲覧

### 2.2 非機能要件（Non-functional Requirements）

#### 2.2.1 パフォーマンス
- ページ読み込み時間: 3秒以内
- API レスポンスタイム: 1秒以内
- 同時接続ユーザー数: 100名以上対応

#### 2.2.2 セキュリティ
- HTTPS通信の必須化
- パスワードのハッシュ化保存
- XSS、CSRF対策の実装
- セッション管理の適切な実装
- 個人情報の暗号化保存

#### 2.2.3 ユーザビリティ
- レスポンシブデザイン対応（PC・タブレット・スマートフォン）
- 直感的なUI/UX設計
- アクセシビリティ対応（WCAG 2.1 AA準拠）
- プロフェッショナル向けの洗練されたデザイン

#### 2.2.4 可用性
- システム稼働率: 99.9%以上
- 定期メンテナンス時の事前通知

#### 2.2.5 保守性
- コードの可読性・保守性の確保
- ドキュメントの整備
- テストコードの実装

#### 2.2.6 拡張性
- 将来的な機能追加に対応できる設計
- スケーラブルなアーキテクチャ

#### 2.2.7 外部システム連携
- **承認システム連携**
  - API経由での申し込みデータ送信
  - 承認ステータスの定期的な同期
  - エラーハンドリングと再送機能
  - タイムアウト対策
- **データ整合性**
  - トランザクション管理
  - データ不整合時のリカバリー機能
  - 連携ログの記録
- **可用性**
  - 外部システム障害時の対応（キュー機能など）
  - 適切なエラーメッセージの表示

---

## 3. 仕様（Specification）

### 3.1 データ仕様

#### 3.1.1 データベーススキーマ

**users（会員テーブル）**
```
id: UUID (Primary Key)
email: VARCHAR(255) (Unique, Not Null)
password_hash: VARCHAR(255) (Not Null)
name: VARCHAR(100) (Not Null)
name_kana: VARCHAR(100)
company_name: VARCHAR(255)
department: VARCHAR(100)
phone: VARCHAR(20)
postal_code: VARCHAR(10)
address: TEXT
created_at: TIMESTAMP
updated_at: TIMESTAMP
last_login_at: TIMESTAMP
is_active: BOOLEAN (Default: true)
email_verified_at: TIMESTAMP
```

**contracts（契約テーブル）**
```
id: UUID (Primary Key)
user_id: UUID (Foreign Key -> users.id)
plan_id: UUID (Foreign Key -> plans.id)
application_number: VARCHAR(50) (Unique, Not Null)
status: ENUM('draft', 'pending', 'submitted', 'approved', 'rejected', 'active', 'cancelled')
submitted_at: TIMESTAMP
approved_at: TIMESTAMP
rejected_at: TIMESTAMP
rejection_reason: TEXT
external_approval_id: VARCHAR(100) (外部承認システムのID)
contract_start_date: DATE
contract_end_date: DATE
total_reward_amount: DECIMAL(10, 2)
reward_status: ENUM('pending', 'confirmed', 'paid')
created_at: TIMESTAMP
updated_at: TIMESTAMP
```

**plans（プラン・サービステーブル）**
```
id: UUID (Primary Key)
plan_code: VARCHAR(50) (Unique, Not Null)
plan_name: VARCHAR(255) (Not Null)
description: TEXT
base_price: DECIMAL(10, 2)
base_reward: DECIMAL(10, 2) (基本報酬額)
is_active: BOOLEAN (Default: true)
display_order: INTEGER
created_at: TIMESTAMP
updated_at: TIMESTAMP
```

**plan_options（プランオプションテーブル）**
```
id: UUID (Primary Key)
plan_id: UUID (Foreign Key -> plans.id)
option_code: VARCHAR(50) (Not Null)
option_name: VARCHAR(255) (Not Null)
description: TEXT
price: DECIMAL(10, 2)
reward_amount: DECIMAL(10, 2) (このオプションによる追加報酬)
is_required: BOOLEAN (Default: false)
is_active: BOOLEAN (Default: true)
display_order: INTEGER
created_at: TIMESTAMP
updated_at: TIMESTAMP
```

**contract_options（契約オプション関連テーブル）**
```
id: UUID (Primary Key)
contract_id: UUID (Foreign Key -> contracts.id)
plan_option_id: UUID (Foreign Key -> plan_options.id)
quantity: INTEGER (Default: 1)
price: DECIMAL(10, 2)
reward_amount: DECIMAL(10, 2)
created_at: TIMESTAMP
```

**rewards（報酬履歴テーブル）**
```
id: UUID (Primary Key)
user_id: UUID (Foreign Key -> users.id)
contract_id: UUID (Foreign Key -> contracts.id)
reward_type: ENUM('contract', 'bonus', 'adjustment')
amount: DECIMAL(10, 2)
status: ENUM('pending', 'confirmed', 'paid', 'cancelled')
confirmed_at: TIMESTAMP
paid_at: TIMESTAMP
description: TEXT
created_at: TIMESTAMP
updated_at: TIMESTAMP
```

**notifications（通知テーブル）**
```
id: UUID (Primary Key)
user_id: UUID (Foreign Key -> users.id)
type: ENUM('contract_submitted', 'contract_approved', 'contract_rejected', 'reward_confirmed', 'reward_paid', 'system')
title: VARCHAR(255)
message: TEXT
is_read: BOOLEAN (Default: false)
read_at: TIMESTAMP
created_at: TIMESTAMP
```

**external_sync_logs（外部システム連携ログテーブル）**
```
id: UUID (Primary Key)
contract_id: UUID (Foreign Key -> contracts.id)
sync_type: ENUM('submit', 'status_update', 'callback')
request_payload: JSON
response_payload: JSON
status: ENUM('success', 'error', 'retry')
error_message: TEXT
retry_count: INTEGER (Default: 0)
created_at: TIMESTAMP
```

### 3.2 API仕様

#### 3.2.1 内部API（ポータルアプリ用）

**認証関連**
```
POST   /api/auth/register          # 会員登録
POST   /api/auth/login             # ログイン
POST   /api/auth/logout            # ログアウト
POST   /api/auth/refresh           # トークンリフレッシュ
POST   /api/auth/password-reset    # パスワードリセット要求
POST   /api/auth/password-update   # パスワード更新
GET    /api/auth/verify-email      # メールアドレス確認
```

**会員情報関連**
```
GET    /api/users/me               # ログインユーザー情報取得
PUT    /api/users/me               # ユーザー情報更新
GET    /api/users/me/summary       # ダッシュボード用サマリー
```

**プラン関連**
```
GET    /api/plans                  # プラン一覧取得
GET    /api/plans/:id              # プラン詳細取得
GET    /api/plans/:id/options      # プランオプション一覧
POST   /api/plans/simulate-reward  # 報酬シミュレーション
```

**契約申し込み関連**
```
POST   /api/contracts              # 契約申し込み作成（下書き）
GET    /api/contracts              # 契約一覧取得
GET    /api/contracts/:id          # 契約詳細取得
PUT    /api/contracts/:id          # 契約更新（下書き時）
POST   /api/contracts/:id/submit   # 契約申し込み送信
DELETE /api/contracts/:id          # 契約削除（下書き時）
GET    /api/contracts/:id/status   # 契約ステータス取得
```

**報酬関連**
```
GET    /api/rewards                # 報酬履歴一覧
GET    /api/rewards/summary        # 報酬サマリー
GET    /api/rewards/:id            # 報酬詳細
```

**通知関連**
```
GET    /api/notifications          # 通知一覧
PUT    /api/notifications/:id/read # 通知既読
PUT    /api/notifications/read-all # 全通知既読
```

#### 3.2.2 外部システム連携API

**承認システムへの送信**
```
POST   /external/approval-system/contracts
Request:
{
  "application_number": "string",
  "user_info": {
    "email": "string",
    "name": "string",
    "company_name": "string"
  },
  "plan_info": {
    "plan_code": "string",
    "plan_name": "string",
    "options": [
      {
        "option_code": "string",
        "quantity": number
      }
    ]
  },
  "total_amount": number,
  "reward_amount": number,
  "submitted_at": "datetime"
}

Response:
{
  "success": boolean,
  "external_id": "string",
  "status": "string",
  "message": "string"
}
```

**承認システムからのコールバック（Webhook）**
```
POST   /api/webhooks/approval-callback
Request:
{
  "external_id": "string",
  "application_number": "string",
  "status": "approved" | "rejected",
  "approved_at": "datetime",
  "rejection_reason": "string",
  "approver": "string"
}

Response:
{
  "success": boolean,
  "message": "string"
}
```

**ステータス同期（ポーリング）**
```
GET    /external/approval-system/contracts/:external_id/status
Response:
{
  "external_id": "string",
  "status": "string",
  "updated_at": "datetime",
  "approver": "string",
  "rejection_reason": "string"
}
```

### 3.3 画面仕様

#### 3.3.1 画面一覧

1. **LP-001: ログインページ**
2. **LP-002: 会員登録ページ**
3. **LP-003: パスワードリセットページ**
4. **DH-001: ダッシュボード**
5. **PL-001: プラン一覧ページ**
6. **PL-002: プラン詳細ページ**
7. **CT-001: 契約申し込みフォーム**
8. **CT-002: 契約内容確認ページ**
9. **CT-003: 契約完了ページ**
10. **CT-004: 契約一覧ページ**
11. **CT-005: 契約詳細ページ**
12. **RW-001: 報酬履歴ページ**
13. **RW-002: 報酬シミュレーションページ**
14. **PR-001: プロフィール編集ページ**
15. **NT-001: 通知一覧ページ**

#### 3.3.2 画面遷移フロー

```
[ログインページ LP-001]
    ↓ ログイン成功
[ダッシュボード DH-001]
    ├→ [プラン一覧 PL-001] → [プラン詳細 PL-002] → [契約申し込みフォーム CT-001]
    │                                                      ↓
    │                                              [契約内容確認 CT-002]
    │                                                      ↓
    │                                              [契約完了 CT-003]
    │
    ├→ [契約一覧 CT-004] → [契約詳細 CT-005]
    │
    ├→ [報酬履歴 RW-001]
    │
    ├→ [報酬シミュレーション RW-002]
    │
    ├→ [プロフィール編集 PR-001]
    │
    └→ [通知一覧 NT-001]
```

#### 3.3.3 主要画面の詳細仕様

**DH-001: ダッシュボード**
- ヘッダー
  - ロゴ
  - ナビゲーションメニュー
  - 通知アイコン（未読数表示）
  - ユーザーメニュー
- メインコンテンツ
  - ウェルカムメッセージ
  - 報酬サマリーカード
    - 累計報酬額
    - 今月の報酬額
    - 確定待ち報酬額
  - 契約ステータスカード
    - 承認待ち件数
    - 今月の契約件数
    - アクティブな契約数
  - クイックアクション
    - 新規契約申し込みボタン
    - 報酬シミュレーションボタン
  - お知らせ・通知リスト（最新5件）
  - 最近の契約一覧（最新3件）
- サイドバー
  - ナビゲーションメニュー

**CT-001: 契約申し込みフォーム**
- ステップインジケーター（現在のステップ表示）
- Step 1: プラン選択
  - プラン選択ラジオボタン
  - 各プランの詳細情報
  - 基本報酬額表示
- Step 2: オプション選択
  - オプションチェックボックス
  - 各オプションの報酬額表示
  - リアルタイム合計報酬計算表示
- Step 3: 申し込み情報入力
  - 会員情報（自動入力・編集不可）
  - 追加情報入力フィールド
- 右サイドバー（固定表示）
  - 選択内容サマリー
  - 合計金額
  - 予想報酬額（大きく強調表示）
- フッター
  - 戻るボタン
  - 次へボタン

**CT-002: 契約内容確認ページ**
- 確認項目
  - プラン情報
  - 選択オプション
  - 会員情報
  - 合計金額
  - 予想報酬額（強調表示）
- 利用規約
  - 利用規約テキスト（スクロール可能）
  - 同意チェックボックス
- アクション
  - 修正ボタン
  - 申し込みボタン（同意チェック後に有効化）

**RW-001: 報酬履歴ページ**
- フィルター
  - 期間選択
  - ステータス選択
  - 契約種別選択
- サマリーカード
  - 表示期間の合計報酬
  - 確定済み報酬
  - 支払済み報酬
- 報酬履歴テーブル
  - 日付
  - 契約番号
  - プラン名
  - 報酬額
  - ステータス
  - 詳細ボタン
- ページネーション

### 3.4 ビジネスロジック仕様

#### 3.4.1 報酬計算ロジック

**基本報酬計算式**
```
合計報酬 = プラン基本報酬 + Σ(選択オプション報酬 × 数量)
```

**報酬ステータス遷移**
```
pending (申し込み時)
    ↓ 契約承認時
confirmed (確定)
    ↓ 支払処理時
paid (支払済)
```

**報酬確定条件**
- 外部承認システムで契約が承認されること
- 契約が有効になること

#### 3.4.2 契約ステータス遷移

```
draft (下書き)
    ↓ 申し込み送信
submitted (送信済)
    ↓ 外部システム送信成功
pending (承認待ち)
    ↓ 承認/却下
approved (承認済) or rejected (却下)
    ↓ 契約開始日到達（approved時）
active (有効)
    ↓ ユーザー操作 or 契約終了日
cancelled (キャンセル) or 契約終了
```

#### 3.4.3 外部システム連携フロー

**申し込み送信フロー**
1. ユーザーが契約申し込みを送信
2. ポータルDBに契約データを保存（status: submitted）
3. 外部承認システムAPIを呼び出し
4. 成功時：
   - contract.external_approval_id を保存
   - status を pending に更新
   - ユーザーに完了画面表示
5. 失敗時：
   - エラーログ記録
   - リトライキューに追加
   - ユーザーにエラーメッセージ表示

**ステータス同期フロー（2つの方式）**

方式1: Webhook（推奨）
1. 外部システムがステータス変更時にWebhookを送信
2. ポータルがコールバックを受信
3. 契約ステータスを更新
4. 報酬ステータスを更新（承認時）
5. ユーザーに通知を送信

方式2: ポーリング（フォールバック）
1. バッチジョブが定期実行（例：5分ごと）
2. pending状態の契約一覧を取得
3. 各契約の外部IDでステータス照会API呼び出し
4. ステータスが変更されていれば更新
5. ユーザーに通知を送信

#### 3.4.4 セキュリティロジック

**パスワードポリシー**
- 最小8文字
- 大文字・小文字・数字を含む
- 特殊文字を1つ以上含む（推奨）

**ログイン試行制限**
- 5回連続失敗で15分間ロック
- IPアドレス単位でも監視

**セッション管理**
- セッションタイムアウト: 30分（無操作時）
- 絶対タイムアウト: 8時間
- Remember Me: 30日間

**API認証**
- JWT（JSON Web Token）使用
- アクセストークン有効期限: 15分
- リフレッシュトークン有効期限: 7日間

---

## 4. アーキテクチャ（Architecture）

### 4.1 技術スタック

#### 4.1.1 フロントエンド
- **フレームワーク**: Next.js 14 (App Router)
  - React 18
  - TypeScript 5
- **スタイリング**:
  - Tailwind CSS 3
  - shadcn/ui（UIコンポーネントライブラリ）
- **状態管理**:
  - Zustand（グローバル状態）
  - React Query（サーバー状態・キャッシュ）
- **フォーム管理**:
  - React Hook Form
  - Zod（バリデーション）
- **HTTP通信**:
  - Axios
- **その他**:
  - date-fns（日付処理）
  - recharts（グラフ表示）

#### 4.1.2 バックエンド
- **ランタイム**: Node.js 20 LTS
- **フレームワーク**: Next.js 14 API Routes / Express.js
- **言語**: TypeScript 5
- **認証**:
  - NextAuth.js (Auth.js)
  - JWT
  - bcrypt（パスワードハッシュ化）
- **バリデーション**: Zod
- **ORM**: Prisma
- **その他**:
  - node-cron（バッチジョブ）
  - nodemailer（メール送信）

#### 4.1.3 データベース
- **メインDB**: PostgreSQL 16
- **キャッシュ**: Redis 7
  - セッション管理
  - API レートリミット
  - 外部API呼び出しキャッシュ

#### 4.1.4 インフラ・デプロイ
- **ホスティング**: Vercel（推奨）または AWS
- **データベースホスティング**:
  - Vercel Postgres / Supabase / AWS RDS
- **ファイルストレージ**: AWS S3（必要に応じて）
- **CI/CD**: GitHub Actions
- **モニタリング**:
  - Vercel Analytics
  - Sentry（エラートラッキング）

#### 4.1.5 開発ツール
- **パッケージマネージャー**: pnpm
- **Linter/Formatter**:
  - ESLint
  - Prettier
- **テスト**:
  - Jest（ユニットテスト）
  - Playwright（E2Eテスト）
- **型チェック**: TypeScript
- **Git Hooks**: Husky + lint-staged

### 4.2 システムアーキテクチャ

#### 4.2.1 全体構成図

```
┌─────────────────────────────────────────────────────────────┐
│                        クライアント層                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Next.js Frontend (React + TypeScript)              │   │
│  │  - ページコンポーネント                               │   │
│  │  - UIコンポーネント (shadcn/ui)                      │   │
│  │  - 状態管理 (Zustand + React Query)                 │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕ HTTPS
┌─────────────────────────────────────────────────────────────┐
│                       アプリケーション層                       │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Next.js API Routes / Express.js                    │   │
│  │  ┌────────────┐  ┌────────────┐  ┌──────────────┐  │   │
│  │  │  認証API   │  │  会員API   │  │  契約API     │  │   │
│  │  └────────────┘  └────────────┘  └──────────────┘  │   │
│  │  ┌────────────┐  ┌────────────┐  ┌──────────────┐  │   │
│  │  │  報酬API   │  │  通知API   │  │  Webhook API │  │   │
│  │  └────────────┘  └────────────┘  └──────────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  ビジネスロジック層                                    │   │
│  │  - 認証サービス                                        │   │
│  │  - 契約管理サービス                                    │   │
│  │  - 報酬計算サービス                                    │   │
│  │  - 通知サービス                                        │   │
│  │  - 外部連携サービス                                    │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                    ↕                        ↕ HTTPS
┌──────────────────────────────┐   ┌────────────────────────┐
│       データ層                │   │   外部システム         │
│  ┌────────────────────────┐  │   │  ┌──────────────────┐ │
│  │  PostgreSQL            │  │   │  │  承認システムAPI  │ │
│  │  - users               │  │   │  │  - 契約送信       │ │
│  │  - contracts           │  │   │  │  - ステータス照会 │ │
│  │  - plans               │  │   │  └──────────────────┘ │
│  │  - rewards             │  │   └────────────────────────┘
│  │  - notifications       │  │
│  └────────────────────────┘  │
│  ┌────────────────────────┐  │
│  │  Redis                 │  │
│  │  - セッション           │  │
│  │  - キャッシュ           │  │
│  │  - ジョブキュー         │  │
│  └────────────────────────┘  │
└──────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                       バッチ処理層                            │
│  - ステータス同期ジョブ (5分ごと)                             │
│  - 報酬集計ジョブ (日次)                                      │
│  - 通知送信ジョブ                                             │
│  - クリーンアップジョブ                                       │
└─────────────────────────────────────────────────────────────┘
```

#### 4.2.2 ディレクトリ構成

```
prototypeportal/
├── src/
│   ├── app/                      # Next.js App Router
│   │   ├── (auth)/              # 認証関連ページ
│   │   │   ├── login/
│   │   │   ├── register/
│   │   │   └── reset-password/
│   │   ├── (dashboard)/         # ダッシュボード関連
│   │   │   ├── page.tsx         # ダッシュボード
│   │   │   ├── contracts/       # 契約関連ページ
│   │   │   ├── rewards/         # 報酬関連ページ
│   │   │   ├── profile/         # プロフィール
│   │   │   └── notifications/   # 通知
│   │   ├── api/                 # API Routes
│   │   │   ├── auth/
│   │   │   ├── users/
│   │   │   ├── contracts/
│   │   │   ├── plans/
│   │   │   ├── rewards/
│   │   │   ├── notifications/
│   │   │   └── webhooks/
│   │   ├── layout.tsx
│   │   └── page.tsx
│   ├── components/              # Reactコンポーネント
│   │   ├── ui/                  # shadcn/ui コンポーネント
│   │   ├── layouts/             # レイアウトコンポーネント
│   │   ├── features/            # 機能別コンポーネント
│   │   │   ├── auth/
│   │   │   ├── contracts/
│   │   │   ├── rewards/
│   │   │   └── dashboard/
│   │   └── common/              # 共通コンポーネント
│   ├── lib/                     # ユーティリティ・ヘルパー
│   │   ├── db/                  # データベース関連
│   │   │   ├── prisma.ts
│   │   │   └── redis.ts
│   │   ├── api/                 # API クライアント
│   │   ├── auth/                # 認証ロジック
│   │   ├── validations/         # Zodスキーマ
│   │   └── utils/               # 汎用ユーティリティ
│   ├── services/                # ビジネスロジック
│   │   ├── auth.service.ts
│   │   ├── user.service.ts
│   │   ├── contract.service.ts
│   │   ├── reward.service.ts
│   │   ├── notification.service.ts
│   │   └── external-api.service.ts
│   ├── types/                   # TypeScript型定義
│   │   ├── models/
│   │   ├── api/
│   │   └── common/
│   └── hooks/                   # カスタムReact Hooks
├── prisma/
│   ├── schema.prisma            # Prismaスキーマ
│   ├── migrations/              # マイグレーション
│   └── seed.ts                  # シードデータ
├── public/                      # 静的ファイル
├── tests/                       # テスト
│   ├── unit/
│   ├── integration/
│   └── e2e/
├── .env.example                 # 環境変数テンプレート
├── .env.local                   # ローカル環境変数
├── next.config.js
├── tailwind.config.ts
├── tsconfig.json
├── package.json
└── README.md
```

### 4.3 セキュリティアーキテクチャ

#### 4.3.1 認証・認可フロー

```
1. ログイン
   ユーザー入力 → API /api/auth/login
   → ユーザー検証
   → JWTトークン生成（アクセス + リフレッシュ）
   → HTTPOnly Cookie にトークン保存
   → クライアントへ返却

2. API リクエスト
   クライアント → API (Cookie に JWT)
   → ミドルウェアでトークン検証
   → ユーザー情報抽出
   → 権限チェック
   → ビジネスロジック実行

3. トークンリフレッシュ
   アクセストークン期限切れ
   → /api/auth/refresh 呼び出し
   → リフレッシュトークン検証
   → 新しいアクセストークン発行
```

#### 4.3.2 外部API連携のセキュリティ

- **API Key管理**: 環境変数で管理、リポジトリにコミットしない
- **通信の暗号化**: HTTPS/TLS 1.3以上
- **リクエスト署名**: HMAC-SHA256での署名検証
- **IPホワイトリスト**: 承認システムとの通信を特定IPに制限
- **レート制限**: Redis を使用したレート制限実装
- **タイムアウト設定**: 外部API呼び出しに適切なタイムアウト設定

### 4.4 パフォーマンス最適化

#### 4.4.1 フロントエンド最適化

- **コード分割**: Next.js の dynamic import 活用
- **画像最適化**: Next.js Image コンポーネント使用
- **キャッシング**:
  - React Query でサーバーデータキャッシュ
  - SWR パターンでデータ更新
- **レンダリング最適化**:
  - Server Components 活用
  - クライアントコンポーネントは必要最小限に

#### 4.4.2 バックエンド最適化

- **データベースクエリ最適化**:
  - インデックス設定
  - N+1問題の回避
  - Prisma の include/select 最適化
- **API レスポンスキャッシング**:
  - Redis でキャッシュ
  - 適切な TTL 設定
- **非同期処理**:
  - メール送信は非同期キューで処理
  - 外部API呼び出しのタイムアウト処理

### 4.5 スケーラビリティ

- **水平スケーリング**: Vercel の自動スケーリング活用
- **データベース**: コネクションプーリング設定
- **キャッシュ戦略**: Redis クラスター化（必要に応じて）
- **CDN活用**: 静的ファイルのCDN配信

---

## 5. 実装計画（Implementation Plan）

### 5.1 実装フェーズ

#### Phase 0: プロジェクトセットアップ（1-2日）
**目標**: 開発環境の構築と基本設定

**タスク**:
1. Next.js プロジェクト初期化
   - `pnpm create next-app@latest`
   - TypeScript, Tailwind CSS, App Router 有効化
2. 開発ツール設定
   - ESLint, Prettier 設定
   - Husky + lint-staged 設定
3. 依存パッケージインストール
   - UI: shadcn/ui
   - 状態管理: zustand, @tanstack/react-query
   - フォーム: react-hook-form, zod
   - ORM: Prisma
   - 認証: next-auth
4. ディレクトリ構成作成
5. 環境変数設定（.env.example 作成）
6. Git リポジトリ設定

**成果物**:
- プロジェクト骨格
- README.md
- .env.example
- 開発環境設定ファイル

---

#### Phase 1: データベース・認証基盤（3-4日）
**目標**: データベーススキーマと認証機能の実装

**タスク**:
1. **Prisma スキーマ定義**
   - users テーブル
   - plans テーブル
   - contracts テーブル
   - rewards テーブル
   - notifications テーブル
   - external_sync_logs テーブル
2. **マイグレーション実行**
   - `npx prisma migrate dev`
3. **シードデータ作成**
   - サンプルプラン・オプションデータ
   - テスト用ユーザー
4. **NextAuth.js 設定**
   - Credentials Provider 設定
   - JWT 設定
   - セッション管理
5. **認証API実装**
   - POST /api/auth/register
   - POST /api/auth/login
   - POST /api/auth/logout
   - POST /api/auth/password-reset
6. **認証ミドルウェア実装**
   - JWT検証ミドルウェア
   - 認証保護ルート設定

**成果物**:
- Prisma スキーマファイル
- マイグレーションファイル
- 認証API
- 認証ミドルウェア

---

#### Phase 2: 基本UI・レイアウト（2-3日）
**目標**: 共通UIコンポーネントとレイアウトの実装

**タスク**:
1. **shadcn/ui コンポーネント導入**
   - Button, Input, Card, Table など
2. **共通レイアウト実装**
   - ヘッダーコンポーネント
   - サイドバーナビゲーション
   - フッター
3. **認証ページ実装**
   - LP-001: ログインページ
   - LP-002: 会員登録ページ
   - LP-003: パスワードリセットページ
4. **レスポンシブ対応**
5. **ローディング・エラー状態**

**成果物**:
- 認証画面（ログイン、登録、パスワードリセット）
- 共通レイアウトコンポーネント
- UIコンポーネントライブラリ

---

#### Phase 3: ダッシュボード・会員機能（3-4日）
**目標**: ダッシュボードとプロフィール管理機能の実装

**タスク**:
1. **会員情報API実装**
   - GET /api/users/me
   - PUT /api/users/me
   - GET /api/users/me/summary
2. **ダッシュボード実装（DH-001）**
   - 報酬サマリーカード
   - 契約ステータスカード
   - お知らせリスト
   - クイックアクション
3. **プロフィール編集ページ（PR-001）**
   - フォーム実装
   - バリデーション
4. **通知機能**
   - 通知一覧ページ（NT-001）
   - 通知API実装
   - リアルタイム通知表示

**成果物**:
- ダッシュボード画面
- プロフィール編集画面
- 通知機能
- 会員情報管理API

---

#### Phase 4: プラン・契約申し込み機能（5-6日）
**目標**: 契約申し込みの中核機能実装

**タスク**:
1. **プランAPI実装**
   - GET /api/plans（プラン一覧）
   - GET /api/plans/:id（プラン詳細）
   - GET /api/plans/:id/options（オプション一覧）
   - POST /api/plans/simulate-reward（報酬シミュレーション）
2. **契約API実装**
   - POST /api/contracts（契約作成）
   - GET /api/contracts（契約一覧）
   - GET /api/contracts/:id（契約詳細）
   - PUT /api/contracts/:id（契約更新）
   - POST /api/contracts/:id/submit（契約送信）
3. **プラン一覧ページ（PL-001）**
4. **プラン詳細ページ（PL-002）**
5. **契約申し込みフォーム（CT-001）**
   - Step 1: プラン選択
   - Step 2: オプション選択
   - Step 3: 申し込み情報入力
   - リアルタイム報酬計算
6. **契約内容確認ページ（CT-002）**
7. **契約完了ページ（CT-003）**
8. **契約一覧ページ（CT-004）**
9. **契約詳細ページ（CT-005）**

**成果物**:
- プラン表示機能
- 契約申し込みフロー（全画面）
- 契約管理API
- 報酬計算ロジック

---

#### Phase 5: 報酬管理機能（2-3日）
**目標**: 報酬表示・履歴機能の実装

**タスク**:
1. **報酬API実装**
   - GET /api/rewards（報酬履歴）
   - GET /api/rewards/summary（報酬サマリー）
   - GET /api/rewards/:id（報酬詳細）
2. **報酬計算サービス実装**
   - 報酬計算ロジック
   - 報酬ステータス管理
3. **報酬履歴ページ（RW-001）**
   - フィルター機能
   - ページネーション
   - グラフ表示
4. **報酬シミュレーションページ（RW-002）**

**成果物**:
- 報酬履歴画面
- 報酬シミュレーション画面
- 報酬管理API
- 報酬計算サービス

---

#### Phase 6: 外部システム連携（4-5日）
**目標**: 承認システムとの連携機能実装

**タスク**:
1. **外部API連携サービス実装**
   - 承認システムAPI クライアント
   - リトライロジック
   - エラーハンドリング
2. **契約送信処理実装**
   - 外部システムへのデータ送信
   - 送信ログ記録
3. **Webhook エンドポイント実装**
   - POST /api/webhooks/approval-callback
   - 署名検証
   - ステータス更新処理
4. **ステータス同期バッチ実装**
   - node-cron でスケジューリング
   - ポーリング処理
   - ステータス更新
5. **通知送信処理**
   - 承認/却下時の通知
   - メール送信

**成果物**:
- 外部API連携サービス
- Webhook エンドポイント
- ステータス同期バッチ
- 通知システム

---

#### Phase 7: テスト実装（3-4日）
**目標**: 品質保証のためのテスト実装

**タスク**:
1. **ユニットテスト**
   - ビジネスロジックのテスト
   - ユーティリティ関数のテスト
   - 報酬計算ロジックのテスト
2. **統合テスト**
   - API エンドポイントのテスト
   - データベース操作のテスト
3. **E2Eテスト（Playwright）**
   - ログインフロー
   - 契約申し込みフロー
   - 報酬確認フロー
4. **テストカバレッジ確認**
   - 目標: 80%以上

**成果物**:
- ユニットテストスイート
- 統合テストスイート
- E2Eテストスイート
- テストドキュメント

---

#### Phase 8: セキュリティ・パフォーマンス改善（2-3日）
**目標**: セキュリティ強化とパフォーマンス最適化

**タスク**:
1. **セキュリティ対策**
   - XSS対策確認
   - CSRF対策実装
   - SQLインジェクション対策確認
   - セキュリティヘッダー設定
   - レート制限実装
2. **パフォーマンス最適化**
   - データベースインデックス設定
   - N+1問題の解消
   - キャッシング戦略実装
   - 画像最適化
3. **セキュリティ監査**
   - 依存パッケージの脆弱性チェック
   - `npm audit` 実行

**成果物**:
- セキュリティ強化済みアプリ
- パフォーマンス最適化
- セキュリティ監査レポート

---

#### Phase 9: デプロイ準備・CI/CD（2日）
**目標**: 本番環境へのデプロイ準備

**タスク**:
1. **環境変数設定**
   - 本番環境用 .env 設定
   - Vercel 環境変数設定
2. **CI/CD パイプライン構築**
   - GitHub Actions 設定
   - 自動テスト実行
   - 自動デプロイ設定
3. **データベース設定**
   - 本番DBセットアップ
   - マイグレーション実行
   - シードデータ投入
4. **モニタリング設定**
   - Sentry 設定
   - Vercel Analytics 設定
5. **ドキュメント整備**
   - デプロイ手順書
   - 運用マニュアル

**成果物**:
- CI/CDパイプライン
- 本番環境設定
- デプロイドキュメント

---

#### Phase 10: 最終調整・リリース（1-2日）
**目標**: 本番リリース

**タスク**:
1. **最終動作確認**
   - 全機能の動作テスト
   - クロスブラウザテスト
   - モバイル対応確認
2. **パフォーマンステスト**
   - 負荷テスト
   - レスポンスタイム確認
3. **本番デプロイ**
   - Vercel へのデプロイ
   - DNS設定
4. **リリース後監視**
   - エラーログ監視
   - パフォーマンス監視

**成果物**:
- 本番稼働アプリケーション
- リリースノート
- 監視体制

---

### 5.2 実装の優先順位

**必須（MVP）**:
1. 認証機能（ログイン・登録）
2. ダッシュボード
3. プラン表示
4. 契約申し込み機能
5. 報酬表示機能
6. 外部システム連携（基本）

**重要**:
7. 報酬履歴・詳細
8. 契約ステータス管理
9. 通知機能
10. プロフィール編集

**推奨**:
11. 報酬シミュレーション
12. 高度なフィルタリング
13. グラフ・分析機能

### 5.3 開発スケジュール概算

- **Phase 0**: 1-2日
- **Phase 1**: 3-4日
- **Phase 2**: 2-3日
- **Phase 3**: 3-4日
- **Phase 4**: 5-6日
- **Phase 5**: 2-3日
- **Phase 6**: 4-5日
- **Phase 7**: 3-4日
- **Phase 8**: 2-3日
- **Phase 9**: 2日
- **Phase 10**: 1-2日

**合計**: 28-38日（約1.5-2ヶ月）

### 5.4 リスクと対策

| リスク | 影響度 | 対策 |
|-------|--------|------|
| 外部システムAPI仕様が未確定 | 高 | モックAPIを先行実装、後で差し替え |
| データベース設計の変更 | 中 | Prismaマイグレーション活用、柔軟な設計 |
| セキュリティ脆弱性 | 高 | 早期からセキュリティレビュー実施 |
| パフォーマンス問題 | 中 | 早期に負荷テスト実施、ボトルネック特定 |
| スケジュール遅延 | 中 | MVP機能に絞って優先実装 |

### 5.5 次のアクション

1. ✅ 仕様書レビュー・承認
2. Phase 0 開始: プロジェクトセットアップ
3. 週次進捗レビュー設定
4. 外部システムAPI仕様の確定

---

**ドキュメント履歴**
- 2025-11-09: 初版作成（目的定義）
- 2025-11-09: 要件定義追加（機能要件・非機能要件）
- 2025-11-09: 報酬管理機能、外部承認システム連携の要件追加
- 2025-11-09: 詳細仕様追加（データ仕様、API仕様、画面仕様、ビジネスロジック）
- 2025-11-09: アーキテクチャ追加（技術スタック、システム構成、セキュリティ設計）
- 2025-11-09: 実装計画追加（Phase 0-10、スケジュール、リスク対策）
