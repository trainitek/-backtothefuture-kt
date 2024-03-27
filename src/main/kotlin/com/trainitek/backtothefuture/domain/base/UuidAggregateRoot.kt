package com.trainitek.backtothefuture.domain.base

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.UUID

@MappedSuperclass
abstract class UuidAggregateRoot : JpaAggregateRoot<UUID> {
    @Id
    override val id: UUID

    constructor() {
        id = UUID.randomUUID()
    }

    constructor(id: UUID) {
        this.id = id
    }
}
