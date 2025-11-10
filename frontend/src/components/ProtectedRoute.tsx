import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/auth';

/**
 * 認証が必要なルート用のコンポーネント
 */
interface ProtectedRouteProps {
  children: React.ReactNode;
}

export const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const { isAuthenticated } = useAuthStore();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};
