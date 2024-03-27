package com.trainitek.backtothefuture

import org.slf4j.LoggerFactory
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.sql.Timestamp
import java.util.Date
class HowDoWeCreateDates {

    companion object {
        private val log = LoggerFactory.getLogger(HowDoWeCreateDates::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            // 1. Using java.util.Date
            log.info("Date: {}", Date())

            // 2. Using LocalDateTime
            log.info("LocalDateTime: {}", LocalDateTime.now())

            log.info("LocalDateTime with UTC zone: {}", LocalDateTime.now(ZoneId.of("UTC")))

            // 3. Using java.sql.Timestamp
            log.info("Timestamp: {}", Timestamp(System.currentTimeMillis()))

            // 4. Using Instant
            log.info("Instant: {}", Instant.now())

            val instantWithClock = Instant.now(Clock.systemUTC())
            log.info("Instant with Clock (UTC): {}", instantWithClock)

            // 5. Using Clock
            val clock = Clock.systemDefaultZone()
            log.info("Default Zone: {}", clock.zone)
            log.info("Clock (Default Zone): {}", clock.instant())

            val clockUTC = Clock.system(ZoneId.of("UTC"))
            log.info("UTC Zone: {}", clockUTC.zone)
            log.info("Clock (UTC): {}", clockUTC.instant())
        }
    }
}