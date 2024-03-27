package com.trainitek.backtothefuture.domain

import com.trainitek.backtothefuture.domain.base.UuidAggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import java.time.Clock
import java.time.Instant

@Entity
class Enrollment(
        @ManyToOne
        val student: User,

        @ManyToOne
        val enroller: User,

        @ManyToOne
        val course: Course,

        val enrolledAt: Instant,
        val availableFrom: Instant,
        var startedAt: Instant? = null,
        var completedAt: Instant? = null,
        var completionValidUntil: Instant? = null
) : UuidAggregateRoot() {

    companion object {
        const val COMPLETION_VALID_DURATION = 24

        fun initialEnrollment(
                student: User,
                enroller: User,
                course: Course,
                enrolledAt: Instant,
                availableFrom: Instant,
                clock: Clock
        ): Enrollment {
            // Use the secondary constructor that accepts a Clock
            return Enrollment(
                    student = student,
                    enroller = enroller,
                    course = course,
                    enrolledAt = enrolledAt,
                    availableFrom = availableFrom,
                    startedAt = null,
                    completedAt = null,
                    clock = clock
            )
        }
    }

    constructor(
            student: User,
            enroller: User,
            course: Course,
            enrolledAt: Instant,
            availableFrom: Instant,
            startedAt: Instant?,
            completedAt: Instant?,
            clock: Clock
    ) : this(student, enroller, course, enrolledAt, availableFrom, startedAt, completedAt) {
        // Include logic from the original Java constructor here
        // For example, check dates against the clock and initialize properties
        if (startedAt != null) {
            require(startedAt.isAfter(availableFrom)) {
                "Started date must be after available date."
            }
            doStartAt(startedAt, clock)
        }
        if (completedAt != null) {
            requireNotNull(startedAt) { "Cannot complete an enrollment that hasn't started." }
            require(completedAt.isAfter(startedAt)) {
                "Completion date must be after start date."
            }
            doCompleteAt(completedAt, clock)
        }
    }

    private fun doStartAt(startedAt: Instant, clock: Clock) {
        this.startedAt = startedAt
        // Logic for starting enrollment
    }

    private fun doCompleteAt(completedAt: Instant, clock: Clock) {
        this.completedAt = completedAt
        this.completionValidUntil = completedAt.atZone(clock.zone).plusMonths(COMPLETION_VALID_DURATION.toLong()).toInstant()
        // Logic for completing enrollment
    }


    fun startAt(clock: Clock) {
        val now = Instant.now(clock)
        check(now.isAfter(availableFrom)) { "Cannot start the enrollment. Current date $now is before available date $availableFrom." }

        startedAt = now
//        addEvent(EnrollmentStarted(id, now, ClassBasedMetaData.metaData(EnrollmentStarted::class.java, clock)))
    }

    fun completeAt(clock: Clock) {
        val now = Instant.now(clock)
        check(isStarted()) { "Cannot complete an enrollment that hasn't started." }
        check(now.isAfter(startedAt)) { "Cannot complete the enrollment. Completion date $now is not after started date $startedAt." }

        completedAt = now
        completionValidUntil = now.atZone(clock.zone).plusMonths(COMPLETION_VALID_DURATION.toLong()).toInstant()
//        addEvent(EnrollmentCompleted(id, now, ClassBasedMetaData.metaData(EnrollmentCompleted::class.java, clock)))
    }

    fun isStarted() = startedAt != null
    fun isCompleted() = completedAt != null
}
