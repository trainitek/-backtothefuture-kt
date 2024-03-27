package com.trainitek.backtothefuture.domain

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface EnrollmentRepository : CrudRepository<Enrollment, UUID>
