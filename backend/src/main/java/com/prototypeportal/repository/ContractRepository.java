package com.prototypeportal.repository;

import com.prototypeportal.entity.Contract;
import com.prototypeportal.entity.ContractStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 契約リポジトリ
 */
@ApplicationScoped
@Transactional
public class ContractRepository {

    @PersistenceContext(unitName = "PrototypePortalPU")
    private EntityManager entityManager;

    /**
     * 契約を保存
     */
    public Contract save(Contract contract) {
        if (contract.getId() == null) {
            entityManager.persist(contract);
            return contract;
        } else {
            return entityManager.merge(contract);
        }
    }

    /**
     * IDで契約を取得
     */
    public Optional<Contract> findById(UUID id) {
        try {
            Contract contract = entityManager.find(Contract.class, id);
            return Optional.ofNullable(contract);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * 申込番号で契約を取得
     */
    public Optional<Contract> findByApplicationNumber(String applicationNumber) {
        try {
            Contract contract = entityManager
                .createQuery("SELECT c FROM Contract c WHERE c.applicationNumber = :applicationNumber", Contract.class)
                .setParameter("applicationNumber", applicationNumber)
                .getSingleResult();
            return Optional.of(contract);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * ユーザーIDで契約一覧を取得
     */
    public List<Contract> findByUserId(UUID userId) {
        return entityManager
            .createQuery("SELECT c FROM Contract c WHERE c.user.id = :userId ORDER BY c.createdAt DESC", Contract.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    /**
     * ユーザーIDとステータスで契約一覧を取得
     */
    public List<Contract> findByUserIdAndStatus(UUID userId, ContractStatus status) {
        return entityManager
            .createQuery("SELECT c FROM Contract c WHERE c.user.id = :userId AND c.status = :status ORDER BY c.createdAt DESC", Contract.class)
            .setParameter("userId", userId)
            .setParameter("status", status)
            .getResultList();
    }

    /**
     * プランIDで契約一覧を取得
     */
    public List<Contract> findByPlanId(UUID planId) {
        return entityManager
            .createQuery("SELECT c FROM Contract c WHERE c.plan.id = :planId ORDER BY c.createdAt DESC", Contract.class)
            .setParameter("planId", planId)
            .getResultList();
    }

    /**
     * ステータスで契約一覧を取得
     */
    public List<Contract> findByStatus(ContractStatus status) {
        return entityManager
            .createQuery("SELECT c FROM Contract c WHERE c.status = :status ORDER BY c.createdAt DESC", Contract.class)
            .setParameter("status", status)
            .getResultList();
    }

    /**
     * 外部承認IDで契約を取得
     */
    public Optional<Contract> findByExternalApprovalId(String externalApprovalId) {
        try {
            Contract contract = entityManager
                .createQuery("SELECT c FROM Contract c WHERE c.externalApprovalId = :externalApprovalId", Contract.class)
                .setParameter("externalApprovalId", externalApprovalId)
                .getSingleResult();
            return Optional.of(contract);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * 全ての契約を取得
     */
    public List<Contract> findAll() {
        return entityManager
            .createQuery("SELECT c FROM Contract c ORDER BY c.createdAt DESC", Contract.class)
            .getResultList();
    }

    /**
     * 契約を削除
     */
    public void delete(Contract contract) {
        if (entityManager.contains(contract)) {
            entityManager.remove(contract);
        } else {
            entityManager.remove(entityManager.merge(contract));
        }
    }

    /**
     * 申込番号が存在するかチェック
     */
    public boolean existsByApplicationNumber(String applicationNumber) {
        Long count = entityManager
            .createQuery("SELECT COUNT(c) FROM Contract c WHERE c.applicationNumber = :applicationNumber", Long.class)
            .setParameter("applicationNumber", applicationNumber)
            .getSingleResult();
        return count > 0;
    }
}
