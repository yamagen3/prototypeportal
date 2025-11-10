import { http, HttpResponse } from 'msw'

/**
 * MSW API ハンドラー
 * テスト用のモック API レスポンスを定義
 */

const API_BASE_URL = 'http://localhost:8080/api/v1'

export const handlers = [
  // ログイン
  http.post(`${API_BASE_URL}/auth/login`, async ({ request }) => {
    const body = await request.json() as { email: string; password: string }

    if (body.email === 'test@example.com' && body.password === 'password123') {
      return HttpResponse.json({
        data: {
          user: {
            id: '1',
            email: 'test@example.com',
            name: 'Test User',
            role: 'MEMBER',
            createdAt: '2024-01-01T00:00:00Z',
            updatedAt: '2024-01-01T00:00:00Z',
          },
          tokens: {
            accessToken: 'mock-access-token',
            refreshToken: 'mock-refresh-token',
          },
        },
      })
    }

    return HttpResponse.json(
      {
        message: 'Invalid credentials',
        code: 'INVALID_CREDENTIALS',
      },
      { status: 401 }
    )
  }),

  // ユーザー登録
  http.post(`${API_BASE_URL}/auth/register`, async ({ request }) => {
    const body = await request.json() as { email: string; password: string; name: string }

    return HttpResponse.json({
      data: {
        user: {
          id: '2',
          email: body.email,
          name: body.name,
          role: 'MEMBER',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        },
        tokens: {
          accessToken: 'mock-access-token',
          refreshToken: 'mock-refresh-token',
        },
      },
    })
  }),

  // 現在のユーザー情報取得
  http.get(`${API_BASE_URL}/auth/me`, () => {
    return HttpResponse.json({
      data: {
        id: '1',
        email: 'test@example.com',
        name: 'Test User',
        role: 'MEMBER',
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      },
    })
  }),

  // ログアウト
  http.post(`${API_BASE_URL}/auth/logout`, () => {
    return HttpResponse.json({ message: 'Logged out successfully' })
  }),
]
