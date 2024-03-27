package com.trainitek.backtothefuture.application

import com.trainitek.backtothefuture.domain.*
import com.trainitek.backtothefuture.test.Fixtures
import io.kotest.extensions.clock.TestClock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import kotlin.time.Duration.Companion.days

@SpringBootTest
class MoreComplicatedScenarioTest {

    @Autowired
    private lateinit var startEnrollmentHandler: StartEnrollmentHandler

    @Autowired
    private lateinit var completeEnrollmentHandler: CompleteEnrollmentHandler

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @Autowired
    private lateinit var enrollmentRepository: EnrollmentRepository

    @Autowired
    private lateinit var clock: TestClock

    private lateinit var student: User
    private lateinit var course: Course

    @BeforeEach
    fun setUp() {
        student = Fixtures.someStudent().also { userRepository.save(it) }
        course = Fixtures.someCourse().also { courseRepository.save(it) }
    }

    @Test
    fun `should create a simple enrollment and start it`() {
        // Given an initial enrollment available from Tomorrow
        val today = date("2023-09-11")
        clock.setInstant(today) // Adjust as necessary for your TestClock implementation
        val tomorrow = date("2023-09-12")
        val enrollment = initialEnrollment(today, tomorrow).also { enrollmentRepository.save(it) }

        // When
        assertThatThrownBy { startEnrollmentHandler.start(enrollment.id) }
                .hasMessageContaining("Cannot start the enrollment")

        // Move into the future by 2 days and start
        clock.plus(2.days)
        startEnrollmentHandler.start(enrollment.id)

        // Verify that it's started
        assertEnrollmentIsStarted(enrollment)

        // Move back in time by 2 days and check completion
        clock.minus(2.days)
        assertThatThrownBy { completeEnrollmentHandler.complete(enrollment.id) }
                .hasMessageContaining("Cannot complete the enrollment")

        // Move into the future by 3 days and complete
        clock.plus(3.days)
        completeEnrollmentHandler.complete(enrollment.id)

        // Verify completion
        assertThatEnrollmentIsCompleted(enrollment)
    }

    fun date(dateString: String): Instant {
        return Instant.parse(dateString + "T00:10:00Z")
    }

    private fun initialEnrollment(enrolledAt: Instant, availableFrom: Instant): Enrollment {
        return Fixtures.initialEnrollment(
                enrolledAt = enrolledAt,
                availableFrom = availableFrom,
                clock = clock,
                student = student,
                enroller = student,
                course = course
        )
    }

    private fun assertEnrollmentIsStarted(enrollment: Enrollment) {
        val foundEnrollment = enrollmentRepository.findById(enrollment.id).orElseThrow()
        assertThat(foundEnrollment.isStarted()).isTrue()
    }

    private fun assertThatEnrollmentIsCompleted(enrollment: Enrollment) {
        val foundEnrollment = enrollmentRepository.findById(enrollment.id).orElseThrow()
        assertThat(foundEnrollment.isCompleted()).isTrue()
    }
}
