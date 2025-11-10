import { test, expect } from '@playwright/test';

/**
 * 契約申し込みフローのE2Eテスト
 */

test.describe('Contract Flow', () => {
  // 認証が必要なテストのため、実際の認証実装後に有効化
  test.skip('should show contract creation page', async ({ page }) => {
    // 本来はログイン処理が必要
    await page.goto('/contracts/new');

    await expect(page.locator('h1')).toContainText('新規契約申し込み');
    await expect(page.locator('text=1. プラン選択')).toBeVisible();
  });

  test.skip('should show contract list page', async ({ page }) => {
    await page.goto('/contracts');

    await expect(page.locator('h1')).toContainText('契約一覧');
  });
});
