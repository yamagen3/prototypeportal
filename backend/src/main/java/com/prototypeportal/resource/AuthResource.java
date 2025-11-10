package com.prototypeportal.resource;

import com.prototypeportal.dto.AuthResponseDto;
import com.prototypeportal.dto.LoginDto;
import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * 認証関連のREST APIリソース
 *
 * エンドポイント:
 * - POST /api/v1/auth/register - ユーザー登録
 * - POST /api/v1/auth/login - ログイン
 * - POST /api/v1/auth/logout - ログアウト
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthService authService;

    /**
     * ユーザー登録
     *
     * @param dto ユーザー登録情報
     * @return 登録されたユーザー情報とJWTトークン
     */
    @POST
    @Path("/register")
    public Response register(@Valid UserRegistrationDto dto) {
        AuthResponseDto response = authService.register(dto);
        return Response
            .status(Response.Status.CREATED)
            .entity(ApiResponse.success(response, "User registered successfully"))
            .build();
    }

    /**
     * ログイン
     *
     * @param dto ログイン情報
     * @return ユーザー情報とJWTトークン
     */
    @POST
    @Path("/login")
    public Response login(@Valid LoginDto dto) {
        AuthResponseDto response = authService.login(dto);
        return Response
            .ok(ApiResponse.success(response, "Login successful"))
            .build();
    }

    /**
     * ログアウト
     *
     * @return ログアウト成功メッセージ
     */
    @POST
    @Path("/logout")
    public Response logout() {
        // TODO: トークンの無効化処理を実装
        return Response
            .ok(ApiResponse.success(null, "Logout successful"))
            .build();
    }
}
