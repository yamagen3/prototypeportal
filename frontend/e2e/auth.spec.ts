import { test, expect } from '@playwright/test';

/**
 * 認証フローのE2Eテスト
 */

test.describe('Authentication Flow', () => {
  test('should show login page', async ({ page }) => {
    await page.goto('/login');
    await expect(page).toHaveTitle(/Portal/);
    await expect(page.locator('h2')).toContainText('ログイン');
  });

  test('should show validation errors on empty submit', async ({ page }) => {
    await page.goto('/login');
    await page.click('button[type="submit"]');

    // バリデーションエラーが表示されることを確認
    await expect(page.locator('text=有効なメールアドレスを入力してください')).toBeVisible();
  });

  test('should navigate to register page', async ({ page }) => {
    await page.goto('/login');
    await page.click('text=新規会員登録');

    await expect(page).toHaveURL(/.*register/);
    await expect(page.locator('h2')).toContainText('新規会員登録');
  });

  test('should show register form', async ({ page }) => {
    await page.goto('/register');

    await expect(page.locator('input[name="name"]')).toBeVisible();
    await expect(page.locator('input[name="email"]')).toBeVisible();
    await expect(page.locator('input[name="password"]')).toBeVisible();
    await expect(page.locator('input[name="confirmPassword"]')).toBeVisible();
  });
});
