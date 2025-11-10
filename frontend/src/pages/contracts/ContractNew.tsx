import { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { planService } from '../../services/plan.service';
import { contractService } from '../../services/contract.service';
import type { Plan, Option } from '../../types';

/**
 * 契約申し込みページ（簡略版）
 */
export const ContractNew = () => {
  const navigate = useNavigate();
  const [selectedPlanId, setSelectedPlanId] = useState<string>('');
  const [selectedOptions, setSelectedOptions] = useState<Record<string, number>>({});

  // プラン一覧を取得
  const { data: plans, isLoading: isLoadingPlans } = useQuery({
    queryKey: ['plans'],
    queryFn: () => planService.getPlans(),
  });

  // 選択されたプランのオプションを取得
  const { data: options } = useQuery({
    queryKey: ['planOptions', selectedPlanId],
    queryFn: () => planService.getPlanOptions(selectedPlanId),
    enabled: !!selectedPlanId,
  });

  // 契約作成
  const createMutation = useMutation({
    mutationFn: (data: { planId: string; options: { planOptionId: string; quantity: number }[] }) =>
      contractService.createContract(data),
    onSuccess: () => {
      alert('契約を作成しました');
      navigate('/contracts');
    },
  });

  const handleSubmit = () => {
    if (!selectedPlanId) {
      alert('プランを選択してください');
      return;
    }

    const optionsArray = Object.entries(selectedOptions).map(([planOptionId, quantity]) => ({
      planOptionId,
      quantity,
    }));

    createMutation.mutate({
      planId: selectedPlanId,
      options: optionsArray,
    });
  };

  return (
    <div className="max-w-4xl space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">新規契約申し込み</h1>
        <p className="mt-1 text-sm text-gray-600">プランとオプションを選択してください</p>
      </div>

      {/* プラン選択 */}
      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-lg font-medium text-gray-900 mb-4">1. プラン選択</h2>
        {isLoadingPlans ? (
          <p className="text-gray-500">読み込み中...</p>
        ) : (
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            {plans?.map((plan: Plan) => (
              <div
                key={plan.id}
                onClick={() => setSelectedPlanId(plan.id)}
                className={`cursor-pointer p-4 border-2 rounded-lg transition-colors ${
                  selectedPlanId === plan.id
                    ? 'border-blue-500 bg-blue-50'
                    : 'border-gray-300 hover:border-gray-400'
                }`}
              >
                <h3 className="font-medium text-gray-900">{plan.name}</h3>
                <p className="text-sm text-gray-500 mt-1">{plan.description}</p>
                <p className="text-lg font-semibold text-blue-600 mt-2">
                  基本報酬: ¥{plan.baseReward.toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* オプション選択 */}
      {selectedPlanId && options && (
        <div className="bg-white shadow rounded-lg p-6">
          <h2 className="text-lg font-medium text-gray-900 mb-4">2. オプション選択（任意）</h2>
          <div className="space-y-4">
            {options.map((option: Option) => (
              <div key={option.id} className="flex items-center justify-between p-4 border rounded-lg">
                <div className="flex-1">
                  <h3 className="font-medium text-gray-900">{option.name}</h3>
                  <p className="text-sm text-gray-500">{option.description}</p>
                  <p className="text-sm font-semibold text-blue-600 mt-1">
                    報酬: ¥{option.reward.toLocaleString()} / 個
                  </p>
                </div>
                <div className="ml-4">
                  <input
                    type="number"
                    min="0"
                    max="10"
                    value={selectedOptions[option.id] || 0}
                    onChange={(e) =>
                      setSelectedOptions((prev) => ({
                        ...prev,
                        [option.id]: parseInt(e.target.value) || 0,
                      }))
                    }
                    className="w-20 px-3 py-2 border border-gray-300 rounded-md"
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* アクションボタン */}
      <div className="flex justify-end space-x-4">
        <button
          onClick={() => navigate('/dashboard')}
          className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
        >
          キャンセル
        </button>
        <button
          onClick={handleSubmit}
          disabled={!selectedPlanId || createMutation.isPending}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {createMutation.isPending ? '作成中...' : '契約を作成'}
        </button>
      </div>
    </div>
  );
};
