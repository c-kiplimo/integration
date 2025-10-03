package com.collicode.integration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(
    exclude = [KafkaAutoConfiguration::class]
)
class IntegrationApplication

fun main(args: Array<String>) {
    runApplication<IntegrationApplication>(*args)
}
