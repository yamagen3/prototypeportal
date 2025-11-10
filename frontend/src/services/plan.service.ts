import { apiClient, unwrapApiResponse } from '../lib/api-client';
import type { Plan, Option, ApiResponse } from '../types';

/**
 * プラン API サービス
 */

export const planService = {
  /**
   * アクティブなプラン一覧を取得
   */
  async getPlans(): Promise<Plan[]> {
    const response = await apiClient.get<ApiResponse<Plan[]>>('/plans');
    return unwrapApiResponse(response.data);
  },

  /**
   * プラン詳細を取得
   */
  async getPlan(id: string): Promise<Plan> {
    const response = await apiClient.get<ApiResponse<Plan>>(`/plans/${id}`);
    return unwrapApiResponse(response.data);
  },

  /**
   * プランのオプション一覧を取得
   */
  async getPlanOptions(planId: string): Promise<Option[]> {
    const response = await apiClient.get<ApiResponse<Option[]>>(`/plans/${planId}/options`);
    return unwrapApiResponse(response.data);
  },

  /**
   * 報酬をシミュレート
   */
  async simulateReward(data: {
    planId: string;
    options: { planOptionId: string; quantity: number }[];
  }): Promise<{
    planId: string;
    planName: string;
    planBaseReward: number;
    options: Array<{
      planOptionId: string;
      optionName: string;
      quantity: number;
      unitReward: number;
      totalReward: number;
    }>;
    totalOptionsReward: number;
    totalReward: number;
  }> {
    const response = await apiClient.post('/plans/simulate-reward', data);
    return unwrapApiResponse(response.data);
  },
};
