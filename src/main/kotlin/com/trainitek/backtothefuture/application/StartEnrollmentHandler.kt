package com.trainitek.backtothefuture.application

import com.trainitek.backtothefuture.domain.EnrollmentRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.UUID

@Service
@Transactional
class StartEnrollmentHandler(
    private val repository: EnrollmentRepository,
    private val clock: Clock
) {
    fun start(enrollmentId: UUID) {
        val enrollment = repository.findById(enrollmentId).orElseThrow {
            EntityNotFoundException("Enrollment id=$enrollmentId not found")
        }
        enrollment.startAt(clock)
        repository.save(enrollment)
    }
}
