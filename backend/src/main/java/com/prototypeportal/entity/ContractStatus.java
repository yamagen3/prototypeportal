package com.prototypeportal.entity;

/**
 * 契約ステータス
 */
public enum ContractStatus {
    /**
     * 下書き
     */
    DRAFT,

    /**
     * 保留中
     */
    PENDING,

    /**
     * 申込済み
     */
    SUBMITTED,

    /**
     * 承認済み
     */
    APPROVED,

    /**
     * 却下
     */
    REJECTED,

    /**
     * 契約中（有効）
     */
    ACTIVE,

    /**
     * キャンセル済み
     */
    CANCELLED
}
