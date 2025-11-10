import { render, RenderOptions } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactElement, ReactNode } from 'react'
import { BrowserRouter } from 'react-router-dom'

/**
 * テスト用カスタムレンダー関数
 *
 * React Query と React Router のプロバイダーでラップ
 */

interface CustomRenderOptions extends Omit<RenderOptions, 'wrapper'> {
  initialRoute?: string
}

function createTestQueryClient() {
  return new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
        staleTime: Infinity,
      },
      mutations: {
        retry: false,
      },
    },
    logger: {
      log: console.log,
      warn: console.warn,
      error: () => {}, // テスト時はエラーログを抑制
    },
  })
}

interface AllTheProvidersProps {
  children: ReactNode
}

function AllTheProviders({ children }: AllTheProvidersProps) {
  const queryClient = createTestQueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>{children}</BrowserRouter>
    </QueryClientProvider>
  )
}

function customRender(
  ui: ReactElement,
  options?: CustomRenderOptions
) {
  const { initialRoute = '/', ...renderOptions } = options || {}

  if (initialRoute !== '/') {
    window.history.pushState({}, 'Test page', initialRoute)
  }

  return render(ui, {
    wrapper: AllTheProviders,
    ...renderOptions,
  })
}

// re-export everything
export * from '@testing-library/react'
export { customRender as render }
