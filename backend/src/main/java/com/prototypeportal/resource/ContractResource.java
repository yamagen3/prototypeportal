package com.prototypeportal.resource;

import com.prototypeportal.dto.ContractCreateDto;
import com.prototypeportal.dto.ContractResponseDto;
import com.prototypeportal.entity.ContractStatus;
import com.prototypeportal.service.ContractService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

/**
 * 契約関連のREST APIリソース
 *
 * エンドポイント:
 * - POST /api/v1/contracts - 契約作成
 * - GET /api/v1/contracts - 契約一覧取得
 * - GET /api/v1/contracts/{id} - 契約詳細取得
 * - PUT /api/v1/contracts/{id} - 契約更新
 * - POST /api/v1/contracts/{id}/submit - 契約申し込み
 */
@Path("/contracts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContractResource {

    @Inject
    private ContractService contractService;

    /**
     * 新規契約を作成
     * TODO: JWT認証からユーザーIDを取得
     *
     * @param dto 契約作成情報
     * @return 作成された契約情報
     */
    @POST
    public Response createContract(@Valid ContractCreateDto dto) {
        // TODO: JWT から userId を取得
        // 仮実装: ダミーユーザーID
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        ContractResponseDto response = contractService.createContract(userId, dto);
        return Response
            .status(Response.Status.CREATED)
            .entity(ApiResponse.success(response, "Contract created successfully"))
            .build();
    }

    /**
     * ログインユーザーの契約一覧を取得
     * TODO: JWT認証からユーザーIDを取得
     *
     * @param status フィルター用ステータス（オプション）
     * @return 契約一覧
     */
    @GET
    public Response getContracts(@QueryParam("status") ContractStatus status) {
        // TODO: JWT から userId を取得
        // 仮実装: ダミーユーザーID
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        List<ContractResponseDto> contracts;
        if (status != null) {
            contracts = contractService.findByUserIdAndStatus(userId, status);
        } else {
            contracts = contractService.findByUserId(userId);
        }

        return Response
            .ok(ApiResponse.success(contracts))
            .build();
    }

    /**
     * IDで契約詳細を取得
     *
     * @param id 契約ID
     * @return 契約詳細
     */
    @GET
    @Path("/{id}")
    public Response getContractById(@PathParam("id") UUID id) {
        ContractResponseDto contract = contractService.findById(id);
        return Response
            .ok(ApiResponse.success(contract))
            .build();
    }

    /**
     * 契約を更新（下書き状態のみ）
     *
     * @param id 契約ID
     * @param dto 更新情報
     * @return 更新された契約情報
     */
    @PUT
    @Path("/{id}")
    public Response updateContract(@PathParam("id") UUID id, @Valid ContractCreateDto dto) {
        ContractResponseDto response = contractService.updateContract(id, dto);
        return Response
            .ok(ApiResponse.success(response, "Contract updated successfully"))
            .build();
    }

    /**
     * 契約を申し込む
     *
     * @param id 契約ID
     * @return 申し込み済み契約情報
     */
    @POST
    @Path("/{id}/submit")
    public Response submitContract(@PathParam("id") UUID id) {
        ContractResponseDto response = contractService.submitContract(id);
        return Response
            .ok(ApiResponse.success(response, "Contract submitted successfully"))
            .build();
    }
}
