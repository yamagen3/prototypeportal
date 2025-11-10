import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/auth';
import { authService } from '../services/auth.service';
import type { AuthCredentials } from '../types';

/**
 * 認証カスタムフック
 */
export const useAuth = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated, setUser, setTokens, logout: logoutStore } = useAuthStore();

  // ログイン
  const loginMutation = useMutation({
    mutationFn: (credentials: AuthCredentials) => authService.login(credentials),
    onSuccess: (data) => {
      setUser(data.user);
      setTokens(data.tokens);
      navigate('/dashboard');
    },
  });

  // 会員登録
  const registerMutation = useMutation({
    mutationFn: (data: { email: string; password: string; name: string }) =>
      authService.register(data),
    onSuccess: (data) => {
      setUser(data.user);
      setTokens(data.tokens);
      navigate('/dashboard');
    },
  });

  // ログアウト
  const logoutMutation = useMutation({
    mutationFn: () => authService.logout(),
    onSuccess: () => {
      logoutStore();
      navigate('/login');
    },
    onError: () => {
      // エラーでも強制ログアウト
      logoutStore();
      navigate('/login');
    },
  });

  return {
    user,
    isAuthenticated,
    login: loginMutation.mutate,
    register: registerMutation.mutate,
    logout: logoutMutation.mutate,
    isLoggingIn: loginMutation.isPending,
    isRegistering: registerMutation.isPending,
    loginError: loginMutation.error,
    registerError: registerMutation.error,
  };
};
