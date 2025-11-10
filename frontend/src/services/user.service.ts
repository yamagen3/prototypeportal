import { apiClient, unwrapApiResponse } from '../lib/api-client';
import type { User, ApiResponse } from '../types';

/**
 * ユーザー API サービス
 */

export const userService = {
  /**
   * 現在のユーザー情報を取得
   */
  async getCurrentUser(): Promise<User> {
    const response = await apiClient.get<ApiResponse<User>>('/users/me');
    return unwrapApiResponse(response.data);
  },

  /**
   * ユーザー情報を更新
   */
  async updateUser(data: Partial<User>): Promise<User> {
    const response = await apiClient.put<ApiResponse<User>>('/users/me', data);
    return unwrapApiResponse(response.data);
  },
};
