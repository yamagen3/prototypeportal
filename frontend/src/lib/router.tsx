import { createBrowserRouter, Navigate } from 'react-router-dom';
import { ProtectedRoute } from '../components/ProtectedRoute';
import { DashboardLayout } from '../components/layout/DashboardLayout';
import { Login } from '../pages/auth/Login';
import { Register } from '../pages/auth/Register';

/**
 * ルーター設定
 */

// 仮のコンポーネント（後で実装）
const DashboardPage = () => <div>Dashboard Page - 実装予定</div>;
const ContractNewPage = () => <div>New Contract Page - 実装予定</div>;
const ContractListPage = () => <div>Contract List Page - 実装予定</div>;
const RewardPage = () => <div>Reward Page - 実装予定</div>;
const ProfilePage = () => <div>Profile Page - 実装予定</div>;

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Navigate to="/dashboard" replace />,
  },
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/register',
    element: <Register />,
  },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <DashboardLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        path: 'dashboard',
        element: <DashboardPage />,
      },
      {
        path: 'contracts',
        children: [
          {
            index: true,
            element: <ContractListPage />,
          },
          {
            path: 'new',
            element: <ContractNewPage />,
          },
        ],
      },
      {
        path: 'rewards',
        element: <RewardPage />,
      },
      {
        path: 'profile',
        element: <ProfilePage />,
      },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/dashboard" replace />,
  },
]);
