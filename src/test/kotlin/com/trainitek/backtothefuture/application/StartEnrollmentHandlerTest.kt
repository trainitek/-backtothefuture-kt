package com.trainitek.backtothefuture.application

import com.trainitek.backtothefuture.config.MutableClockTestExecutionListener
import com.trainitek.backtothefuture.domain.*
import com.trainitek.backtothefuture.test.Fixtures
import io.kotest.extensions.clock.TestClock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.days

@SpringBootTest
@TestExecutionListeners(
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
    listeners = [MutableClockTestExecutionListener::class]
)
class StartEnrollmentHandlerTest {

    @Autowired
    private lateinit var handler: StartEnrollmentHandler

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
    fun `should not allow to start enrollment too early`() {
        // Given
        val enrolledAt = Instant.parse("2023-09-10T00:00:00Z")
        clock.setInstant(enrolledAt)
        val availableFrom = enrolledAt.plus(Duration.ofDays(1))
        val enrollment = Fixtures.initialEnrollment(
            student = student,
            enroller = student,
            course = course,
            enrolledAt = enrolledAt,
            availableFrom = availableFrom,
            clock = clock
        ).also { enrollmentRepository.save(it) }

        // When & Then
        assertThatThrownBy { handler.start(enrollment.id) }
            .hasMessageContaining("Cannot start the enrollment")
    }

    @Test
    fun `should create a simple enrollment and start it`() {
        // Given
        val enrolledAt = Instant.parse("2023-09-11T00:00:00Z")
        clock.setInstant(enrolledAt)
        val availableFrom = enrolledAt.plus(Duration.ofDays(1))
        val enrollment = Fixtures.initialEnrollment(
            student = student,
            enroller = student,
            course = course,
            enrolledAt = enrolledAt,
            availableFrom = availableFrom,
            clock = clock
        ).also { enrollmentRepository.save(it) }

        // When
        clock.plus(2.days)
        handler.start(enrollment.id)

        // Then
        val foundEnrollment = enrollmentRepository.findById(enrollment.id).orElseThrow()
        assertThat(foundEnrollment.isStarted()).isTrue()
    }
}
