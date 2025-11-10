import { setupServer } from 'msw/node'
import { handlers } from './handlers'

/**
 * MSW サーバーセットアップ
 * Node.js 環境（テスト環境）用
 */
export const server = setupServer(...handlers)
