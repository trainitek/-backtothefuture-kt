package com.trainitek.backtothefuture.domain.base

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version

@MappedSuperclass
abstract class JpaAggregateRoot<ID> : AggregateRoot<ID> {
    @Version
    val version: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AggregateRoot<*>
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}
