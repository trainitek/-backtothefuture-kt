package com.trainitek.backtothefuture.application

import com.trainitek.backtothefuture.domain.EnrollmentRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.UUID

@Service
@Transactional
class CompleteEnrollmentHandler(
    private val repository: EnrollmentRepository,
    private val clock: Clock
) {
    fun complete(enrollmentId: UUID) {
        val enrollment = repository.findById(enrollmentId).orElseThrow {
            EntityNotFoundException("Enrollment id=$enrollmentId not found")
        }
        enrollment.completeAt(clock)
        repository.save(enrollment)
    }
}
