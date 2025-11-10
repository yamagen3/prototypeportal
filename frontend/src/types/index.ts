/**
 * 共通型定義
 */

// ユーザー関連
// ユーザー関連
export interface User {
  id: string;
  email: string;
  name: string;
  role?: UserRole;
  companyName?: string;
  department?: string;
  phone?: string;
  postalCode?: string;
  address?: string;
  createdAt: string;
  updatedAt: string;
}

export type UserRole = 'MEMBER' | 'ADMIN';
}

export type UserRole = 'MEMBER' | 'ADMIN';

export interface AuthCredentials {
  email: string;
  password: string;
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

// 契約関連
export interface Contract {
  id: string;
  userId: string;
  planId: string;
  status: ContractStatus;
  externalId?: string;
  selectedOptions: ContractOption[];
  totalReward: number;
  submittedAt: string;
  approvedAt?: string;
  rejectedAt?: string;
}

export type ContractStatus =
  | 'DRAFT'
  | 'SUBMITTED'
  | 'PENDING'
  | 'APPROVED'
  | 'REJECTED'
  | 'CANCELLED';

export interface ContractOption {
  optionId: string;
  quantity: number;
}

// プラン関連
export interface Plan {
  id: string;
  name: string;
  description: string;
  baseReward: number;
  isActive: boolean;
  displayOrder: number;
}

export interface Option {
  id: string;
  planId: string;
  name: string;
  description: string;
  reward: number;
  isActive: boolean;
  displayOrder: number;
}

// 報酬関連
export interface Reward {
  id: string;
  userId: string;
  contractId: string;
  amount: number;
  status: RewardStatus;
  confirmedAt?: string;
}

export type RewardStatus = 'PENDING' | 'CONFIRMED' | 'PAID';

// 通知関連
export interface Notification {
  id: string;
  userId: string;
  type: NotificationType;
  title: string;
  message: string;
  isRead: boolean;
  createdAt: string;
}

export type NotificationType =
  | 'CONTRACT_APPROVED'
  | 'CONTRACT_REJECTED'
  | 'REWARD_CONFIRMED';

// API レスポンス
export interface ApiResponse<T> {
  data: T;
  message?: string;
}

export interface ApiError {
  message: string;
  code: string;
  details?: Record<string, string[]>;
}

export interface PaginatedResponse<T> {
  items: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}
