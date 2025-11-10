import { apiClient, unwrapApiResponse } from '../lib/api-client';
import type { Contract, ApiResponse } from '../types';

/**
 * 契約 API サービス
 */

export const contractService = {
  /**
   * 契約一覧を取得
   */
  async getContracts(status?: string): Promise<Contract[]> {
    const params = status ? { status } : {};
    const response = await apiClient.get<ApiResponse<Contract[]>>('/contracts', { params });
    return unwrapApiResponse(response.data);
  },

  /**
   * 契約詳細を取得
   */
  async getContract(id: string): Promise<Contract> {
    const response = await apiClient.get<ApiResponse<Contract>>(`/contracts/${id}`);
    return unwrapApiResponse(response.data);
  },

  /**
   * 新規契約を作成
   */
  async createContract(data: {
    planId: string;
    options: { planOptionId: string; quantity: number }[];
  }): Promise<Contract> {
    const response = await apiClient.post<ApiResponse<Contract>>('/contracts', data);
    return unwrapApiResponse(response.data);
  },

  /**
   * 契約を更新
   */
  async updateContract(
    id: string,
    data: { planId: string; options: { planOptionId: string; quantity: number }[] }
  ): Promise<Contract> {
    const response = await apiClient.put<ApiResponse<Contract>>(`/contracts/${id}`, data);
    return unwrapApiResponse(response.data);
  },

  /**
   * 契約を申し込む
   */
  async submitContract(id: string): Promise<Contract> {
    const response = await apiClient.post<ApiResponse<Contract>>(`/contracts/${id}/submit`);
    return unwrapApiResponse(response.data);
  },
};
