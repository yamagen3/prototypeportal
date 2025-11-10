# Prototype Portal

プロフェッショナル向けB2B/B2B2P会員契約ポータルアプリケーション

## 概要

既存サービス会員が新たな契約を行う際に使用するポータルアプリケーションです。契約に伴う報酬情報の透明な提示と、外部承認システムとのシームレスな連携を実現します。

## アーキテクチャ

- **フロントエンド**: React 18 + TypeScript（SPA）
- **バックエンド**: Jakarta EE 10 + Java 17（REST API）
- **データベース**: PostgreSQL 16

## ドキュメント

| ドキュメント | 説明 |
|-------------|------|
| [SPEC.md](./SPEC.md) | **仕様書**：目的、要件、詳細仕様、実装計画 |
| [ARCHITECTURE.md](./ARCHITECTURE.md) | **アーキテクチャ決定記録**：技術選定の理由と背景 |

## 主要機能

- **会員管理**: 登録、ログイン、プロフィール編集
- **契約申し込み**: プラン選択、オプション選択、リアルタイム報酬計算
- **報酬管理**: 報酬履歴、シミュレーション、ステータス確認
- **契約ステータス管理**: 外部承認システム連携、ステータス同期
- **通知**: 契約承認・却下通知、報酬確定通知

## プロジェクト構成

```
prototypeportal/
├── frontend/              # React 18 + TypeScript + Vite SPA
│   ├── src/
│   │   ├── components/    # 再利用可能コンポーネント
│   │   ├── features/      # 機能別モジュール
│   │   ├── lib/           # ユーティリティ・設定
│   │   ├── services/      # API サービス
│   │   ├── store/         # Zustand ストア
│   │   └── types/         # TypeScript 型定義
│   └── package.json
├── backend/               # Jakarta EE 10 + Java 17 REST API
│   ├── src/main/java/com/prototypeportal/
│   │   ├── config/        # アプリケーション設定
│   │   ├── entity/        # JPA エンティティ
│   │   ├── repository/    # データアクセス層
│   │   ├── service/       # ビジネスロジック
│   │   ├── resource/      # JAX-RS リソース（API）
│   │   ├── dto/           # データ転送オブジェクト
│   │   ├── security/      # 認証・認可
│   │   └── exception/     # 例外ハンドリング
│   └── pom.xml
├── docker-compose.yml     # PostgreSQL + pgAdmin
├── .env.example           # 環境変数テンプレート
├── SPEC.md                # 仕様書
├── ARCHITECTURE.md        # アーキテクチャ決定記録
└── README.md              # このファイル
```

## 開発環境セットアップ

### 前提条件

- **Node.js**: 18.x 以上
- **Java**: JDK 17 (LTS)
- **Maven**: 3.8 以上
- **Docker & Docker Compose**: 最新版
- **アプリケーションサーバー**: WildFly 27+ / Payara 6+ / GlassFish 7+

### セットアップ手順

#### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd prototypeportal
```

#### 2. 環境変数の設定

```bash
cp .env.example .env
# .env ファイルを編集して必要な値を設定
```

#### 3. データベースの起動

```bash
# PostgreSQL と pgAdmin を起動
docker-compose up -d

# データベースの確認
docker-compose ps

# ログの確認
docker-compose logs postgres
```

**pgAdmin にアクセス**: http://localhost:5050
- Email: `admin@prototypeportal.com`
- Password: `admin`

#### 4. フロントエンド (React) のセットアップ

```bash
cd frontend

# 依存パッケージのインストール
npm install

# 開発サーバーの起動
npm run dev

# ビルド（本番用）
npm run build
```

フロントエンドは http://localhost:3000 で起動します。

#### 5. バックエンド (Java EE) のセットアップ

```bash
cd backend

# Maven 依存関係のインストール
mvn clean install

# データベースマイグレーション（Flyway）
mvn flyway:migrate

# WAR ファイルの作成
mvn package
```

生成された `backend/target/backend.war` をアプリケーションサーバー（WildFly/Payara/GlassFish）にデプロイしてください。

バックエンド API は http://localhost:8080/api/v1 で起動します。

## 開発コマンド

### フロントエンド

```bash
cd frontend

npm run dev              # 開発サーバー起動
npm run build            # 本番用ビルド
npm run preview          # ビルド結果のプレビュー
npm run lint             # ESLint 実行
npm run test             # テスト実行（watch モード）
npm run test:run         # テスト実行（1回のみ）
npm run test:coverage    # カバレッジ付きテスト実行
```

### バックエンド

```bash
cd backend

mvn clean install        # ビルド
mvn test                 # ユニットテスト実行
mvn verify               # 統合テスト含む全テスト実行
mvn test -Dtest=ClassName # 特定のテストクラスを実行
mvn package              # WAR ファイル作成
mvn flyway:migrate       # DB マイグレーション実行
```

### データベース

```bash
# PostgreSQL の起動
docker-compose up -d postgres

# PostgreSQL の停止
docker-compose stop postgres

# データベースのリセット
docker-compose down -v
docker-compose up -d
```

## 開発状況

**Phase 0: プロジェクトセットアップ** - ✅ 完了

- フロントエンド（React + Vite + TypeScript）プロジェクト初期化
- バックエンド（Jakarta EE + Maven）プロジェクト初期化
- Docker Compose（PostgreSQL）設定
- 基本ディレクトリ構成とファイル作成

**次のステップ**: Phase 1（データベース設計とマイグレーション）

詳細は [SPEC.md](./SPEC.md) の「5. 実装計画」を参照してください。

## TDD（テスト駆動開発）

本プロジェクトでは**テスト駆動開発（TDD）**を採用しています。すべての機能実装は以下のサイクルで進めます：

### TDDサイクル

```
1. Red    -> テストを書く（失敗することを確認）
2. Green  -> テストが通る最小限のコードを書く
3. Refactor -> コードをリファクタリングする
```

### テスト環境

#### フロントエンド
- **Vitest**: ユニットテスト実行
- **React Testing Library**: コンポーネントテスト
- **Mock Service Worker (MSW)**: API モック

#### バックエンド
- **JUnit 5**: ユニットテスト
- **Mockito**: モック作成
- **Arquillian**: 統合テスト
- **TestContainers**: データベーステスト

### テストカバレッジ目標

- **行カバレッジ**: 80%以上
- **分岐カバレッジ**: 75%以上
- **ビジネスロジック**: 100%

### TDD開発フロー

1. 機能要件を理解し、受け入れ条件を明確化
2. テストケースを先に実装（Red）
3. テストが通る最小限の実装（Green）
4. コードをリファクタリング（Refactor）
5. テストと実装を一緒にコミット

詳細なTDD戦略とガイドラインは [SPEC.md](./SPEC.md) の「6. 開発方針・プラクティス」を参照してください。

## トラブルシューティング

### フロントエンドが起動しない

```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run dev
```

### バックエンドビルドエラー

```bash
cd backend
mvn clean install -U
```

### データベース接続エラー

```bash
# Docker コンテナの状態確認
docker-compose ps

# PostgreSQL ログの確認
docker-compose logs postgres

# コンテナの再起動
docker-compose restart postgres
```

## ライセンス

TBD
