package com.prototypeportal.repository;

import com.prototypeportal.entity.Plan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Planエンティティのリポジトリ
 */
@ApplicationScoped
@Transactional
public class PlanRepository {

    @PersistenceContext(unitName = "PrototypePortalPU")
    private EntityManager entityManager;

    /**
     * プランを保存
     */
    public Plan save(Plan plan) {
        if (plan.getId() == null) {
            entityManager.persist(plan);
            return plan;
        } else {
            return entityManager.merge(plan);
        }
    }

    /**
     * IDでプランを検索
     */
    public Optional<Plan> findById(UUID id) {
        Plan plan = entityManager.find(Plan.class, id);
        return Optional.ofNullable(plan);
    }

    /**
     * プランコードでプランを検索
     */
    public Optional<Plan> findByPlanCode(String planCode) {
        try {
            Plan plan = entityManager
                .createQuery("SELECT p FROM Plan p WHERE p.planCode = :planCode", Plan.class)
                .setParameter("planCode", planCode)
                .getSingleResult();
            return Optional.of(plan);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * アクティブなプランのみを検索（表示順でソート）
     */
    public List<Plan> findAllActive() {
        return entityManager
            .createQuery("SELECT p FROM Plan p WHERE p.isActive = true ORDER BY p.displayOrder ASC, p.createdAt DESC", Plan.class)
            .getResultList();
    }

    /**
     * すべてのプランを検索
     */
    public List<Plan> findAll() {
        return entityManager
            .createQuery("SELECT p FROM Plan p ORDER BY p.displayOrder ASC, p.createdAt DESC", Plan.class)
            .getResultList();
    }

    /**
     * プランを削除
     */
    public void delete(Plan plan) {
        if (entityManager.contains(plan)) {
            entityManager.remove(plan);
        } else {
            entityManager.remove(entityManager.merge(plan));
        }
    }

    /**
     * IDでプランを削除
     */
    public void deleteById(UUID id) {
        findById(id).ifPresent(this::delete);
    }

    /**
     * プランコードが存在するかチェック
     */
    public boolean existsByPlanCode(String planCode) {
        Long count = entityManager
            .createQuery("SELECT COUNT(p) FROM Plan p WHERE p.planCode = :planCode", Long.class)
            .setParameter("planCode", planCode)
            .getSingleResult();
        return count > 0;
    }
}
