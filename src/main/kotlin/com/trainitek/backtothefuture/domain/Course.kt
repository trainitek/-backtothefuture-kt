package com.trainitek.backtothefuture.domain

import com.trainitek.backtothefuture.domain.base.UuidAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Course(
    val courseName: String,
    @Id @GeneratedValue override var id: UUID = UUID.randomUUID()
) : UuidAggregateRoot(id)
