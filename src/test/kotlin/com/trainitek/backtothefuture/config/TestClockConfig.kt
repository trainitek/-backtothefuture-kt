package com.trainitek.backtothefuture.config

import io.kotest.extensions.clock.TestClock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.ZoneId

@Configuration
class TestClockConfig {
    @Bean
    fun clock(): TestClock = TestClock(Instant.now(), ZoneId.of("UTC"))
}
