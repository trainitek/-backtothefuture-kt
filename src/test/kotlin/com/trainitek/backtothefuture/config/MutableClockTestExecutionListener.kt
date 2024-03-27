package com.trainitek.backtothefuture.config

import io.kotest.extensions.clock.TestClock
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import java.time.Clock
import java.time.ZoneId

class MutableClockTestExecutionListener : AbstractTestExecutionListener() {
    override fun beforeTestMethod(testContext: TestContext) {
        val mutableClock = testContext.applicationContext.getBean(TestClock::class.java)
        mutableClock.setInstant(Clock.system(ZoneId.of("UTC")).instant())
    }
}
