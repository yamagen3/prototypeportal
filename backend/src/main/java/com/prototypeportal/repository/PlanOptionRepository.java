package com.prototypeportal.repository;

import com.prototypeportal.entity.PlanOption;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PlanOptionエンティティのリポジトリ
 */
@ApplicationScoped
@Transactional
public class PlanOptionRepository {

    @PersistenceContext(unitName = "PrototypePortalPU")
    private EntityManager entityManager;

    /**
     * プランオプションを保存
     */
    public PlanOption save(PlanOption planOption) {
        if (planOption.getId() == null) {
            entityManager.persist(planOption);
            return planOption;
        } else {
            return entityManager.merge(planOption);
        }
    }

    /**
     * IDでプランオプションを検索
     */
    public Optional<PlanOption> findById(UUID id) {
        PlanOption planOption = entityManager.find(PlanOption.class, id);
        return Optional.ofNullable(planOption);
    }

    /**
     * プランIDでオプションを検索（アクティブのみ）
     */
    public List<PlanOption> findByPlanIdAndActive(UUID planId) {
        return entityManager
            .createQuery(
                "SELECT po FROM PlanOption po WHERE po.plan.id = :planId AND po.isActive = true ORDER BY po.displayOrder ASC",
                PlanOption.class
            )
            .setParameter("planId", planId)
            .getResultList();
    }

    /**
     * プランIDでオプションを検索（すべて）
     */
    public List<PlanOption> findByPlanId(UUID planId) {
        return entityManager
            .createQuery(
                "SELECT po FROM PlanOption po WHERE po.plan.id = :planId ORDER BY po.displayOrder ASC",
                PlanOption.class
            )
            .setParameter("planId", planId)
            .getResultList();
    }

    /**
     * プランオプションを削除
     */
    public void delete(PlanOption planOption) {
        if (entityManager.contains(planOption)) {
            entityManager.remove(planOption);
        } else {
            entityManager.remove(entityManager.merge(planOption));
        }
    }

    /**
     * IDでプランオプションを削除
     */
    public void deleteById(UUID id) {
        findById(id).ifPresent(this::delete);
    }
}
