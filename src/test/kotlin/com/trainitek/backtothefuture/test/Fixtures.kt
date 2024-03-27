package com.trainitek.backtothefuture.test

import com.trainitek.backtothefuture.domain.Course
import com.trainitek.backtothefuture.domain.Enrollment
import com.trainitek.backtothefuture.domain.User
import java.time.Clock
import java.time.Instant

object Fixtures {
    fun someStudent(): User = User("John", "Doe") // Assuming User has a primary constructor taking firstName and lastName

    fun someCourse(): Course = Course("Trainitek - Implementing Modern Architecture") // Assuming Course has a primary constructor taking courseName

    fun initialEnrollment(
            student: User = someStudent(),
            enroller: User = someStudent(),
            course: Course = someCourse(),
            enrolledAt: Instant,
            availableFrom: Instant,
            clock: Clock
    ): Enrollment {
        // Use the secondary constructor of Enrollment that includes Clock
        return Enrollment.initialEnrollment(
                student = student,
                enroller = enroller,
                course = course,
                enrolledAt = enrolledAt,
                availableFrom = availableFrom,
                clock = clock
        )
    }

    fun startedEnrollment(
            student: User = someStudent(),
            enroller: User = someStudent(),
            course: Course = someCourse(),
            enrolledAt: Instant,
            availableFrom: Instant,
            startedAt: Instant,
            clock: Clock
    ): Enrollment {
        // Create an Enrollment instance and immediately start it
        return Enrollment(
                student = student,
                enroller = enroller,
                course = course,
                enrolledAt = enrolledAt,
                availableFrom = availableFrom,
                startedAt = null, // startedAt is handled within the startAt method
                completedAt = null,
                clock = clock
        ).apply {
            startAt(clock) // Assumes that startAt sets startedAt
        }
    }
}