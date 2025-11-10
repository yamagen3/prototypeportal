package com.prototypeportal.repository;

import com.prototypeportal.entity.ContractOption;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 契約オプションリポジトリ
 */
@ApplicationScoped
@Transactional
public class ContractOptionRepository {

    @PersistenceContext(unitName = "PrototypePortalPU")
    private EntityManager entityManager;

    /**
     * 契約オプションを保存
     */
    public ContractOption save(ContractOption contractOption) {
        if (contractOption.getId() == null) {
            entityManager.persist(contractOption);
            return contractOption;
        } else {
            return entityManager.merge(contractOption);
        }
    }

    /**
     * IDで契約オプションを取得
     */
    public Optional<ContractOption> findById(UUID id) {
        try {
            ContractOption contractOption = entityManager.find(ContractOption.class, id);
            return Optional.ofNullable(contractOption);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * 契約IDで契約オプション一覧を取得
     */
    public List<ContractOption> findByContractId(UUID contractId) {
        return entityManager
            .createQuery("SELECT co FROM ContractOption co WHERE co.contract.id = :contractId", ContractOption.class)
            .setParameter("contractId", contractId)
            .getResultList();
    }

    /**
     * プランオプションIDで契約オプション一覧を取得
     */
    public List<ContractOption> findByPlanOptionId(UUID planOptionId) {
        return entityManager
            .createQuery("SELECT co FROM ContractOption co WHERE co.planOption.id = :planOptionId", ContractOption.class)
            .setParameter("planOptionId", planOptionId)
            .getResultList();
    }

    /**
     * 全ての契約オプションを取得
     */
    public List<ContractOption> findAll() {
        return entityManager
            .createQuery("SELECT co FROM ContractOption co", ContractOption.class)
            .getResultList();
    }

    /**
     * 契約オプションを削除
     */
    public void delete(ContractOption contractOption) {
        if (entityManager.contains(contractOption)) {
            entityManager.remove(contractOption);
        } else {
            entityManager.remove(entityManager.merge(contractOption));
        }
    }
}
