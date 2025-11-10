package com.prototypeportal.resource;

import com.prototypeportal.dto.*;
import com.prototypeportal.service.PlanService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

/**
 * プラン関連のREST APIリソース
 *
 * エンドポイント:
 * - GET /api/v1/plans - プラン一覧取得
 * - GET /api/v1/plans/{id} - プラン詳細取得
 * - GET /api/v1/plans/{id}/options - プランオプション一覧取得
 */
@Path("/plans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlanResource {

    @Inject
    private PlanService planService;

    /**
     * アクティブなプラン一覧を取得
     *
     * @return プラン一覧
     */
    @GET
    public Response getAllActivePlans() {
        List<PlanResponseDto> plans = planService.findAllActivePlans();
        return Response
            .ok(ApiResponse.success(plans))
            .build();
    }

    /**
     * IDでプラン詳細を取得
     *
     * @param id プランID
     * @return プラン詳細
     */
    @GET
    @Path("/{id}")
    public Response getPlanById(@PathParam("id") UUID id) {
        PlanResponseDto plan = planService.findById(id);
        return Response
            .ok(ApiResponse.success(plan))
            .build();
    }

    /**
     * プランコードでプラン詳細を取得
     *
     * @param planCode プランコード
     * @return プラン詳細
     */
    @GET
    @Path("/code/{planCode}")
    public Response getPlanByCode(@PathParam("planCode") String planCode) {
        PlanResponseDto plan = planService.findByPlanCode(planCode);
        return Response
            .ok(ApiResponse.success(plan))
            .build();
    }

    /**
     * プランのアクティブなオプション一覧を取得
     *
     * @param id プランID
     * @return オプション一覧
     */
    @GET
    @Path("/{id}/options")
    public Response getPlanOptions(@PathParam("id") UUID id) {
        List<PlanOptionResponseDto> options = planService.findActiveOptionsByPlanId(id);
        return Response
            .ok(ApiResponse.success(options))
            .build();
    }
}

    /**
     * 報酬シミュレーション
     *
     * @param dto シミュレーションリクエスト
     * @return シミュレーション結果
     */
    @POST
    @Path("/simulate-reward")
    public Response simulateReward(@Valid RewardSimulationRequestDto dto) {
        RewardSimulationResponseDto result = planService.simulateReward(dto);
        return Response
            .ok(ApiResponse.success(result))
            .build();
    }
}
