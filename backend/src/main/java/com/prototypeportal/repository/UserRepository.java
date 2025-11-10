package com.prototypeportal.repository;

import com.prototypeportal.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Userエンティティのリポジトリ
 */
@ApplicationScoped
@Transactional
public class UserRepository {

    @PersistenceContext(unitName = "PrototypePortalPU")
    private EntityManager entityManager;

    /**
     * ユーザーを保存
     */
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    /**
     * IDでユーザーを検索
     */
    public Optional<User> findById(UUID id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * メールアドレスでユーザーを検索
     */
    public Optional<User> findByEmail(String email) {
        try {
            User user = entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * アクティブなユーザーのみを検索
     */
    public List<User> findAllActive() {
        return entityManager
            .createQuery("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.createdAt DESC", User.class)
            .getResultList();
    }

    /**
     * すべてのユーザーを検索
     */
    public List<User> findAll() {
        return entityManager
            .createQuery("SELECT u FROM User u ORDER BY u.createdAt DESC", User.class)
            .getResultList();
    }

    /**
     * ユーザーを削除
     */
    public void delete(User user) {
        if (entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            entityManager.remove(entityManager.merge(user));
        }
    }

    /**
     * IDでユーザーを削除
     */
    public void deleteById(UUID id) {
        findById(id).ifPresent(this::delete);
    }

    /**
     * メールアドレスが存在するかチェック
     */
    public boolean existsByEmail(String email) {
        Long count = entityManager
            .createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
            .setParameter("email", email)
            .getSingleResult();
        return count > 0;
    }

    /**
     * ユーザー数をカウント
     */
    public long count() {
        return entityManager
            .createQuery("SELECT COUNT(u) FROM User u", Long.class)
            .getSingleResult();
    }
}
