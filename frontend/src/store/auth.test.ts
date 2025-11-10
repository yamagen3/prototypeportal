import { describe, it, expect, beforeEach } from 'vitest'
import { useAuthStore } from './auth'

describe('AuthStore', () => {
  beforeEach(() => {
    // 各テスト前にストアをリセット
    useAuthStore.setState({
      user: null,
      isAuthenticated: false,
    })
    localStorage.clear()
  })

  describe('setUser', () => {
    it('should set user and mark as authenticated', () => {
      const mockUser = {
        id: '1',
        email: 'test@example.com',
        name: 'Test User',
        role: 'MEMBER' as const,
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      }

      useAuthStore.getState().setUser(mockUser)

      const state = useAuthStore.getState()
      expect(state.user).toEqual(mockUser)
      expect(state.isAuthenticated).toBe(true)
    })

    it('should mark as not authenticated when user is null', () => {
      useAuthStore.getState().setUser(null)

      const state = useAuthStore.getState()
      expect(state.user).toBeNull()
      expect(state.isAuthenticated).toBe(false)
    })
  })

  describe('setTokens', () => {
    it('should store tokens in localStorage', () => {
      const tokens = {
        accessToken: 'test-access-token',
        refreshToken: 'test-refresh-token',
      }

      useAuthStore.getState().setTokens(tokens)

      expect(localStorage.setItem).toHaveBeenCalledWith(
        'accessToken',
        'test-access-token'
      )
      expect(localStorage.setItem).toHaveBeenCalledWith(
        'refreshToken',
        'test-refresh-token'
      )
    })
  })

  describe('logout', () => {
    it('should clear user state and remove tokens from localStorage', () => {
      // セットアップ
      const mockUser = {
        id: '1',
        email: 'test@example.com',
        name: 'Test User',
        role: 'MEMBER' as const,
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      }
      useAuthStore.getState().setUser(mockUser)

      // ログアウト
      useAuthStore.getState().logout()

      // 検証
      const state = useAuthStore.getState()
      expect(state.user).toBeNull()
      expect(state.isAuthenticated).toBe(false)
      expect(localStorage.removeItem).toHaveBeenCalledWith('accessToken')
      expect(localStorage.removeItem).toHaveBeenCalledWith('refreshToken')
    })
  })
})
