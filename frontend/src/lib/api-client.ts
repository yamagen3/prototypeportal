import axios, { AxiosError } from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import type { ApiError, ApiResponse } from '../types';

/**
 * Axios API クライアント設定
 */

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

// Axios インスタンスを作成
export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// リクエストインターセプター（JWT トークン追加）
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('accessToken');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// レスポンスインターセプター（エラーハンドリング）
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error: AxiosError<ApiError>) => {
    // 401 エラー: トークンリフレッシュまたはログアウト
    if (error.response?.status === 401) {
      // トークンをクリア
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');

      // ログインページへリダイレクト
      window.location.href = '/login';
    }

    return Promise.reject(error);
  }
);

/**
 * API エラーメッセージを取得
 */
export const getApiErrorMessage = (error: unknown): string => {
  if (axios.isAxiosError(error)) {
    const apiError = error.response?.data as ApiError | undefined;
    return apiError?.message || error.message || 'An error occurred';
  }

  if (error instanceof Error) {
    return error.message;
  }

  return 'An unknown error occurred';
};

/**
 * API レスポンスデータを取得
 */
export const unwrapApiResponse = <T>(response: ApiResponse<T>): T => {
  return response.data;
};
