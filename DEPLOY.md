# デプロイガイド

## GitHub Pages デプロイ

### 自動デプロイ設定

このブランチ（`deploy/github-pages`）にプッシュすると、GitHub Actionsが自動的にビルドしてGitHub Pagesにデプロイします。

### URL

デプロイ後のURL: https://yamagen3.github.io/prototypeportal/

### 初回セットアップ手順

1. GitHubリポジトリの設定を開く
2. Settings → Pages に移動
3. Source を「GitHub Actions」に設定
4. このブランチ（deploy/github-pages）をプッシュ

### ローカルでビルド確認

```bash
cd frontend
npm run build
npm run preview
```

### 注意事項

- **バックエンドAPI**: GitHub Pagesは静的サイトのみ対応のため、バックエンドAPIは別途デプロイが必要です
- **APIエンドポイント**: `frontend/src/lib/api-client.ts` の `API_BASE_URL` を本番APIのURLに変更してください
- **環境変数**: `.env.production` ファイルで本番用の設定を行ってください

### バックエンドデプロイ推奨サービス

- **Railway**: https://railway.app （PostgreSQL込み、無料枠あり）
- **Render**: https://render.com （PostgreSQL込み、無料枠あり）
- **Fly.io**: https://fly.io （PostgreSQL別途、無料枠あり）

## 環境変数設定例

### .env.production（フロントエンド用）

```env
VITE_API_BASE_URL=https://your-backend-api.railway.app/api/v1
```

### 本番環境でのバックエンド設定

```properties
# application.properties
spring.datasource.url=jdbc:postgresql://your-db-host:5432/prototypeportal
spring.datasource.username=your-username
spring.datasource.password=your-password
```

## デプロイフロー

```
1. コード変更
   ↓
2. deploy/github-pages ブランチにコミット
   ↓
3. GitHub Actions が自動実行
   ↓
4. ビルド成功 → GitHub Pages にデプロイ
   ↓
5. https://yamagen3.github.io/prototypeportal/ で確認
```

## トラブルシューティング

### ビルドエラーが発生する場合

```bash
cd frontend
npm ci
npm run build
```

ローカルでビルドが成功することを確認してから、プッシュしてください。

### ページが表示されない場合

1. GitHub Actions のログを確認
2. Settings → Pages で「GitHub Actions」が選択されているか確認
3. ブラウザのキャッシュをクリア

### APIエラーが発生する場合

バックエンドAPIが起動していないため、以下の対応が必要です：
1. バックエンドを別のサービスにデプロイ
2. フロントエンドの `VITE_API_BASE_URL` を更新
3. CORS設定をバックエンドに追加
