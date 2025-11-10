# TDD実践ガイド

このドキュメントでは、本プロジェクトにおけるテスト駆動開発（TDD）の実践方法を具体的に説明します。

## 目次

1. [TDDの基本](#tddの基本)
2. [フロントエンドTDD実践](#フロントエンドtdd実践)
3. [バックエンドTDD実践](#バックエンドtdd実践)
4. [ベストプラクティス](#ベストプラクティス)
5. [よくある質問](#よくある質問)

---

## TDDの基本

### TDDサイクル（Red-Green-Refactor）

```
1. Red    → テストを書く（失敗することを確認）
2. Green  → テストが通る最小限のコードを書く
3. Refactor → コードをリファクタリングする
```

### なぜTDDを採用するのか？

- **品質保証**: バグの早期発見とリグレッション防止
- **設計改善**: テスタブルで疎結合なコード
- **ドキュメント**: テストコードが仕様書として機能
- **リファクタリング**: 安全な変更が可能
- **開発効率**: デバッグ時間の削減

---

## フロントエンドTDD実践

### 環境

- **Vitest**: 高速テストランナー
- **React Testing Library**: ユーザー視点のテスト
- **MSW**: API モック

### テスト実行コマンド

```bash
cd frontend

# ウォッチモード（開発時）
npm run test

# 1回のみ実行（CI）
npm run test:run

# カバレッジ付き
npm run test:coverage

# UI モード
npm run test:ui
```

### 例1: Zustand ストアのテスト

**ファイル**: `src/store/auth.test.ts`

```typescript
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

  it('should set user and mark as authenticated', () => {
    const mockUser = {
      id: '1',
      email: 'test@example.com',
      name: 'Test User',
      role: 'MEMBER' as const,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    }

    // Action
    useAuthStore.getState().setUser(mockUser)

    // Assert
    const state = useAuthStore.getState()
    expect(state.user).toEqual(mockUser)
    expect(state.isAuthenticated).toBe(true)
  })
})
```

### 例2: React コンポーネントのテスト

**ステップ1: テストを書く（Red）**

```typescript
// LoginForm.test.tsx
import { render, screen } from '@/test/utils'
import userEvent from '@testing-library/user-event'
import { describe, it, expect, vi } from 'vitest'
import { LoginForm } from './LoginForm'

describe('LoginForm', () => {
  it('should submit valid credentials', async () => {
    const onSubmit = vi.fn()
    render(<LoginForm onSubmit={onSubmit} />)

    await userEvent.type(screen.getByLabelText('Email'), 'test@example.com')
    await userEvent.type(screen.getByLabelText('Password'), 'password123')
    await userEvent.click(screen.getByRole('button', { name: 'Login' }))

    expect(onSubmit).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123',
    })
  })

  it('should show validation error for invalid email', async () => {
    render(<LoginForm onSubmit={vi.fn()} />)

    await userEvent.type(screen.getByLabelText('Email'), 'invalid-email')
    await userEvent.click(screen.getByRole('button', { name: 'Login' }))

    expect(screen.getByText('Invalid email format')).toBeInTheDocument()
  })
})
```

**ステップ2: 実装を書く（Green）**

```typescript
// LoginForm.tsx
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'

const loginSchema = z.object({
  email: z.string().email('Invalid email format'),
  password: z.string().min(1, 'Password is required'),
})

type LoginFormData = z.infer<typeof loginSchema>

interface LoginFormProps {
  onSubmit: (data: LoginFormData) => void
}

export function LoginForm({ onSubmit }: LoginFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  })

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="email">Email</label>
        <input id="email" {...register('email')} />
        {errors.email && <p>{errors.email.message}</p>}
      </div>

      <div>
        <label htmlFor="password">Password</label>
        <input id="password" type="password" {...register('password')} />
        {errors.password && <p>{errors.password.message}</p>}
      </div>

      <button type="submit">Login</button>
    </form>
  )
}
```

**ステップ3: リファクタリング（Refactor）**

- コンポーネントを小さく分割
- スタイリングを追加
- アクセシビリティを改善

### 例3: API サービスのテスト（MSW使用）

```typescript
// auth.service.test.ts
import { describe, it, expect, beforeAll, afterAll, afterEach } from 'vitest'
import { server } from '@/test/mocks/server'
import { authService } from './auth.service'

// MSW サーバーのセットアップ
beforeAll(() => server.listen())
afterEach(() => server.resetHandlers())
afterAll(() => server.close())

describe('AuthService', () => {
  it('should login with valid credentials', async () => {
    const result = await authService.login({
      email: 'test@example.com',
      password: 'password123',
    })

    expect(result.user.email).toBe('test@example.com')
    expect(result.tokens.accessToken).toBeTruthy()
  })

  it('should throw error for invalid credentials', async () => {
    await expect(
      authService.login({
        email: 'wrong@example.com',
        password: 'wrong',
      })
    ).rejects.toThrow()
  })
})
```

---

## バックエンドTDD実践

### 環境

- **JUnit 5**: テストフレームワーク
- **Mockito**: モック作成
- **AssertJ**: 流暢なアサーション
- **TestContainers**: データベーステスト

### テスト実行コマンド

```bash
cd backend

# 全テスト実行
mvn test

# 統合テストも含む
mvn verify

# 特定のテストクラス実行
mvn test -Dtest=StringUtilsTest

# カバレッジレポート生成（JaCoCo）
mvn test jacoco:report
```

### 例1: ユーティリティクラスのテスト

**ステップ1: テストを書く（Red）**

```java
// StringUtilsTest.java
package com.prototypeportal.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StringUtils のテスト")
class StringUtilsTest {

    @Test
    @DisplayName("有効なメールアドレス形式の場合trueを返す")
    void shouldReturnTrueForValidEmail() {
        assertThat(StringUtils.isValidEmail("test@example.com")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "@example.com", "user@"})
    @DisplayName("無効なメールアドレス形式の場合falseを返す")
    void shouldReturnFalseForInvalidEmail(String email) {
        assertThat(StringUtils.isValidEmail(email)).isFalse();
    }
}
```

**ステップ2: 実装を書く（Green）**

```java
// StringUtils.java
package com.prototypeportal.util;

public class StringUtils {

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
```

**ステップ3: リファクタリング（Refactor）**

- エッジケースの追加
- パフォーマンス最適化
- ドキュメント追加

### 例2: サービス層のテスト（Mockito使用）

**ステップ1: テストを書く（Red）**

```java
// UserServiceTest.java
package com.prototypeportal.service;

import com.prototypeportal.entity.User;
import com.prototypeportal.repository.UserRepository;
import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.exception.EmailAlreadyExistsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService のテスト")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("新規ユーザーを登録できる")
    void shouldRegisterNewUser() {
        // Given
        UserRegistrationDto dto = new UserRegistrationDto(
            "test@example.com", "password123", "Test User"
        );

        when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.empty());

        // When
        User user = userService.register(dto);

        // Then
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getName()).isEqualTo("Test User");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("既存のメールアドレスでは登録できない")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        UserRegistrationDto dto = new UserRegistrationDto(
            "existing@example.com", "password123", "Test User"
        );

        when(userRepository.findByEmail(dto.getEmail()))
            .thenReturn(Optional.of(new User()));

        // When & Then
        assertThatThrownBy(() -> userService.register(dto))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessage("Email already registered");
    }
}
```

**ステップ2: 実装を書く（Green）**

```java
// UserService.java
package com.prototypeportal.service;

import com.prototypeportal.entity.User;
import com.prototypeportal.repository.UserRepository;
import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.exception.EmailAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    public User register(UserRegistrationDto dto) {
        // メールアドレスの重複チェック
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        // ユーザーエンティティの作成
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        // パスワードはハッシュ化する（実装省略）

        // 保存
        return userRepository.save(user);
    }
}
```

### 例3: REST APIのテスト（REST Assured使用）

```java
// AuthResourceTest.java
package com.prototypeportal.resource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Auth API のテスト")
class AuthResourceTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";
    }

    @Test
    @DisplayName("有効な認証情報でログインできる")
    void shouldLoginWithValidCredentials() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "email": "test@example.com",
                    "password": "password123"
                }
                """)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .body("data.user.email", equalTo("test@example.com"))
            .body("data.tokens.accessToken", notNullValue());
    }

    @Test
    @DisplayName("無効な認証情報ではログインできない")
    void shouldFailWithInvalidCredentials() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "email": "wrong@example.com",
                    "password": "wrong"
                }
                """)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(401)
            .body("message", containsString("Invalid credentials"));
    }
}
```

---

## ベストプラクティス

### 1. テストの命名規則

#### 良い例
```java
shouldReturnTrueForValidEmail()
shouldThrowExceptionWhenEmailAlreadyExists()
shouldCalculateRewardCorrectly()
```

#### 悪い例
```java
test1()
testEmail()
testMethod()
```

### 2. AAA パターン（Arrange-Act-Assert）

```java
@Test
void shouldCalculateTotalReward() {
    // Arrange（準備）
    Plan plan = new Plan(10000);
    Option option1 = new Option(2000);
    Option option2 = new Option(3000);

    // Act（実行）
    int total = RewardCalculator.calculate(plan, List.of(option1, option2));

    // Assert（検証）
    assertThat(total).isEqualTo(15000);
}
```

### 3. 1つのテストで1つのことだけを検証

#### 良い例
```java
@Test
void shouldValidateEmail() {
    assertThat(StringUtils.isValidEmail("test@example.com")).isTrue();
}

@Test
void shouldValidatePassword() {
    assertThat(StringUtils.isStrongPassword("password123")).isTrue();
}
```

#### 悪い例
```java
@Test
void shouldValidateEverything() {
    assertThat(StringUtils.isValidEmail("test@example.com")).isTrue();
    assertThat(StringUtils.isStrongPassword("password123")).isTrue();
    assertThat(StringUtils.isEmpty("")).isTrue();
    // ... too many assertions
}
```

### 4. テストの独立性

- 各テストは独立して実行可能であるべき
- テスト間で状態を共有しない
- `@BeforeEach` でセットアップ、`@AfterEach` でクリーンアップ

### 5. モックは最小限に

- 必要な部分だけモック化
- 実装の詳細ではなく、インターフェースをテスト
- 統合テストも適切に使用

---

## よくある質問

### Q: テストファーストは必須ですか？

**A**: はい、本プロジェクトではテストファーストを推奨します。ただし、以下の場合は例外：

- プロトタイピング時の探索的コーディング
- 外部ライブラリの動作確認
- 設定ファイルの作成

探索が終わったら、必ずテストを書いてください。

### Q: テストカバレッジは100%を目指すべきですか？

**A**: いいえ、以下の目標を設定しています：

- **行カバレッジ**: 80%以上
- **分岐カバレッジ**: 75%以上
- **ビジネスロジック**: 100%

以下はテスト不要：
- Getter/Setter
- 設定クラス
- 定数定義

### Q: プライベートメソッドはテストしますか？

**A**: いいえ、パブリックAPIを通じてテストします。プライベートメソッドは実装の詳細です。

### Q: テストが遅い場合はどうすればいいですか？

**A**: 以下を検討してください：

1. **ユニットテストと統合テストを分離**
   - ユニットテストは高速に
   - 統合テストは必要な時だけ

2. **並列実行を有効化**
   - Vitest: デフォルトで並列実行
   - JUnit: `@Execution(CONCURRENT)` を使用

3. **不必要なセットアップを削減**
   - テストごとにDB全体をリセットしない
   - 必要なデータだけセットアップ

### Q: リファクタリング時にテストが壊れたらどうしますか？

**A**: 以下を確認：

1. **実装の変更が意図的か？**
   - Yes: テストを更新
   - No: 実装を戻す

2. **テストが実装詳細に依存していないか？**
   - ユーザー視点のテストを書く
   - インターフェースをテスト、実装をテストしない

---

## まとめ

TDDは単なるテスト手法ではなく、設計手法です。

- **テストファースト**で考えることで、より良い設計が生まれる
- **小さなステップ**で進めることで、リスクを最小化
- **継続的なリファクタリング**で、コード品質を維持

困ったときは、このガイドを参照してください。
また、[SPEC.md](./SPEC.md)の「6. 開発方針・プラクティス」も合わせて確認してください。

Happy Testing! 🎯
