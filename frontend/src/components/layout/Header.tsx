import { Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

/**
 * ヘッダーコンポーネント
 */
export const Header = () => {
  const { user, logout } = useAuth();

  return (
    <header className="bg-white shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center">
            <Link to="/dashboard" className="text-xl font-bold text-gray-900">
              Portal
            </Link>
          </div>

          <div className="flex items-center space-x-4">
            <span className="text-sm text-gray-700">{user?.name}</span>
            <button
              onClick={() => logout()}
              className="text-sm text-gray-600 hover:text-gray-900"
            >
              ログアウト
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};
