package com.prototypeportal.resource;

import com.prototypeportal.dto.UserRegistrationDto;
import com.prototypeportal.dto.UserResponseDto;
import com.prototypeportal.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

/**
 * ユーザー関連のREST APIリソース
 *
 * エンドポイント:
 * - GET /api/v1/users/me - ログインユーザー情報取得
 * - PUT /api/v1/users/me - ユーザー情報更新
 * - GET /api/v1/users/{id} - ユーザー情報取得（ID指定）
 * - GET /api/v1/users - ユーザー一覧取得
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    /**
     * ログインユーザー情報を取得
     * TODO: JWT認証からユーザーIDを取得
     *
     * @return ユーザー情報
     */
    @GET
    @Path("/me")
    public Response getCurrentUser() {
        // TODO: JWT から userId を取得
        // 仮実装: ダミーレスポンス
        return Response
            .ok(ApiResponse.success(null, "Get current user - TODO: Implement JWT"))
            .build();
    }

    /**
     * ユーザー情報を更新
     * TODO: JWT認証からユーザーIDを取得
     *
     * @param dto 更新情報
     * @return 更新されたユーザー情報
     */
    @PUT
    @Path("/me")
    public Response updateCurrentUser(@Valid UserRegistrationDto dto) {
        // TODO: JWT から userId を取得
        // 仮実装: ダミーレスポンス
        return Response
            .ok(ApiResponse.success(null, "Update user - TODO: Implement JWT"))
            .build();
    }

    /**
     * IDでユーザー情報を取得
     *
     * @param id ユーザーID
     * @return ユーザー情報
     */
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") UUID id) {
        UserResponseDto user = userService.findById(id);
        return Response
            .ok(ApiResponse.success(user))
            .build();
    }

    /**
     * アクティブなユーザー一覧を取得
     *
     * @return ユーザー一覧
     */
    @GET
    public Response getAllActiveUsers() {
        List<UserResponseDto> users = userService.findAllActive();
        return Response
            .ok(ApiResponse.success(users))
            .build();
    }

    /**
     * ユーザーを無効化
     *
     * @param id ユーザーID
     * @return 成功メッセージ
     */
    @POST
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") UUID id) {
        userService.deactivate(id);
        return Response
            .ok(ApiResponse.success(null, "User deactivated successfully"))
            .build();
    }

    /**
     * ユーザーを有効化
     *
     * @param id ユーザーID
     * @return 成功メッセージ
     */
    @POST
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") UUID id) {
        userService.activate(id);
        return Response
            .ok(ApiResponse.success(null, "User activated successfully"))
            .build();
    }
}
