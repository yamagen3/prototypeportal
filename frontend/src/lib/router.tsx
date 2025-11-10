import { createBrowserRouter, Navigate } from 'react-router-dom';

/**
 * ルーター設定
 */

// 仮のコンポーネント（後で実装）
const LoginPage = () => <div>Login Page</div>;
const RegisterPage = () => <div>Register Page</div>;
const DashboardPage = () => <div>Dashboard Page</div>;
const ContractNewPage = () => <div>New Contract Page</div>;
const ContractListPage = () => <div>Contract List Page</div>;
const RewardPage = () => <div>Reward Page</div>;
const ProfilePage = () => <div>Profile Page</div>;

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Navigate to="/dashboard" replace />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/register',
    element: <RegisterPage />,
  },
  {
    path: '/dashboard',
    element: <DashboardPage />,
  },
  {
    path: '/contracts',
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
    path: '/rewards',
    element: <RewardPage />,
  },
  {
    path: '/profile',
    element: <ProfilePage />,
  },
  {
    path: '*',
    element: <Navigate to="/dashboard" replace />,
  },
]);
