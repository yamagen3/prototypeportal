import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { contractService } from '../services/contract.service';
import { useAuthStore } from '../store/auth';

/**
 * ダッシュボードページ
 */
export const Dashboard = () => {
  const { user } = useAuthStore();

  // 契約一覧を取得
  const { data: contracts, isLoading } = useQuery({
    queryKey: ['contracts'],
    queryFn: () => contractService.getContracts(),
  });

  // ステータス別の契約数を集計
  const contractStats = {
    total: contracts?.length || 0,
    draft: contracts?.filter((c) => c.status === 'DRAFT').length || 0,
    submitted: contracts?.filter((c) => c.status === 'SUBMITTED').length || 0,
    approved: contracts?.filter((c) => c.status === 'APPROVED').length || 0,
  };

  // 合計報酬を計算
  const totalReward =
    contracts
      ?.filter((c) => c.status === 'APPROVED')
      .reduce((sum, c) => sum + c.totalReward, 0) || 0;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">ダッシュボード</h1>
        <p className="mt-1 text-sm text-gray-600">ようこそ、{user?.name}さん</p>
      </div>

      {/* サマリーカード */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl">📋</span>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">総契約数</dt>
                  <dd className="text-lg font-semibold text-gray-900">
                    {contractStats.total}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl">📝</span>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">下書き</dt>
                  <dd className="text-lg font-semibold text-gray-900">
                    {contractStats.draft}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl">⏳</span>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">申込済</dt>
                  <dd className="text-lg font-semibold text-gray-900">
                    {contractStats.submitted}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl">💰</span>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">累計報酬</dt>
                  <dd className="text-lg font-semibold text-gray-900">
                    ¥{totalReward.toLocaleString()}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* クイックアクション */}
      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-lg font-medium text-gray-900 mb-4">クイックアクション</h2>
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <Link
            to="/contracts/new"
            className="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <span className="text-2xl mr-4">📝</span>
            <div>
              <p className="font-medium text-gray-900">新規契約申し込み</p>
              <p className="text-sm text-gray-500">プランを選んで契約を作成</p>
            </div>
          </Link>

          <Link
            to="/contracts"
            className="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <span className="text-2xl mr-4">📋</span>
            <div>
              <p className="font-medium text-gray-900">契約一覧を見る</p>
              <p className="text-sm text-gray-500">すべての契約を確認</p>
            </div>
          </Link>
        </div>
      </div>

      {/* 最近の契約 */}
      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-lg font-medium text-gray-900 mb-4">最近の契約</h2>
        {isLoading ? (
          <p className="text-gray-500">読み込み中...</p>
        ) : contracts && contracts.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    申込番号
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    ステータス
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    報酬額
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    申込日
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {contracts.slice(0, 5).map((contract) => (
                  <tr key={contract.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {contract.id.substring(0, 8)}...
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span
                        className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                          contract.status === 'APPROVED'
                            ? 'bg-green-100 text-green-800'
                            : contract.status === 'SUBMITTED'
                            ? 'bg-yellow-100 text-yellow-800'
                            : 'bg-gray-100 text-gray-800'
                        }`}
                      >
                        {contract.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      ¥{contract.totalReward.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {new Date(contract.submittedAt).toLocaleDateString('ja-JP')}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-gray-500">契約がまだありません</p>
        )}
      </div>
    </div>
  );
};
