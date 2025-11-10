import { test, expect } from '@playwright/test';

/**
 * ダッシュボードのE2Eテスト
 */

test.describe('Dashboard', () => {
  test.skip('should redirect to login when not authenticated', async ({ page }) => {
    await page.goto('/dashboard');

    // 未認証の場合、ログインページにリダイレクトされる
    await expect(page).toHaveURL(/.*login/);
  });

  test.skip('should show dashboard after login', async ({ page }) => {
    // TODO: ログイン処理を実装後に有効化
    // 本来はログイン処理が必要

    await page.goto('/dashboard');
    await expect(page.locator('h1')).toContainText('ダッシュボード');
  });
});
