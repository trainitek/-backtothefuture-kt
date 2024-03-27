package com.trainitek.backtothefuture.domain

import com.trainitek.backtothefuture.domain.base.UuidAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "`user`")
class User(
    val firstName: String,
    val lastName: String,
    @Id @GeneratedValue override var id: UUID = UUID.randomUUID()
) : UuidAggregateRoot(id)
