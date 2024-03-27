package com.trainitek.backtothefuture.application

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDate

@Service
class BackToTheFutureBean(private val clock: Clock) {
    private val log = LoggerFactory.getLogger(BackToTheFutureBean::class.java)

    fun log(msg: String) {
        log.info("({}): Message: \"{}\" at ({})", javaClass.simpleName, msg, LocalDate.now(clock))
    }
}
