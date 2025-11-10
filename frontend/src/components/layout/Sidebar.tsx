import { Link, useLocation } from 'react-router-dom';

/**
 * サイドバーコンポーネント
 */

interface NavItem {
  name: string;
  path: string;
  icon: string;
}

const navItems: NavItem[] = [
  { name: 'ダッシュボード', path: '/dashboard', icon: '📊' },
  { name: '契約申し込み', path: '/contracts/new', icon: '📝' },
  { name: '契約一覧', path: '/contracts', icon: '📋' },
  { name: '報酬履歴', path: '/rewards', icon: '💰' },
  { name: 'プロフィール', path: '/profile', icon: '👤' },
];

export const Sidebar = () => {
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  return (
    <aside className="w-64 bg-white shadow-sm min-h-screen">
      <nav className="mt-5 px-2">
        <div className="space-y-1">
          {navItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`${
                isActive(item.path)
                  ? 'bg-blue-50 border-blue-500 text-blue-700'
                  : 'border-transparent text-gray-600 hover:bg-gray-50 hover:text-gray-900'
              } group flex items-center px-3 py-2 text-sm font-medium border-l-4 transition-colors`}
            >
              <span className="mr-3 text-lg">{item.icon}</span>
              {item.name}
            </Link>
          ))}
        </div>
      </nav>
    </aside>
  );
};
