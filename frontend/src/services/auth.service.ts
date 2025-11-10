import { apiClient, unwrapApiResponse } from '../lib/api-client';
import type { User, AuthCredentials, AuthTokens, ApiResponse } from '../types';

/**
 * 認証 API サービス
 */

export const authService = {
  /**
   * ログイン
   */
  async login(credentials: AuthCredentials): Promise<{ user: User; tokens: AuthTokens }> {
    const response = await apiClient.post<ApiResponse<{ user: User; tokens: AuthTokens }>>(
      '/auth/login',
      credentials
    );
    return unwrapApiResponse(response.data);
  },

  /**
   * 会員登録
   */
  async register(data: {
    email: string;
    password: string;
    name: string;
  }): Promise<{ user: User; tokens: AuthTokens }> {
    const response = await apiClient.post<ApiResponse<{ user: User; tokens: AuthTokens }>>(
      '/auth/register',
      data
    );
    return unwrapApiResponse(response.data);
  },

  /**
   * ログアウト
   */
  async logout(): Promise<void> {
    await apiClient.post('/auth/logout');
  },

  /**
   * 現在のユーザー情報を取得
   */
  async getCurrentUser(): Promise<User> {
    const response = await apiClient.get<ApiResponse<User>>('/auth/me');
    return unwrapApiResponse(response.data);
  },

  /**
   * トークンをリフレッシュ
   */
  async refreshToken(refreshToken: string): Promise<AuthTokens> {
    const response = await apiClient.post<ApiResponse<AuthTokens>>('/auth/refresh', {
      refreshToken,
    });
    return unwrapApiResponse(response.data);
  },
};
