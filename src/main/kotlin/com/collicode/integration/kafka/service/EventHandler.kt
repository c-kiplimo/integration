package com.motus.integration.kafka.service

import reactor.core.publisher.Mono

fun interface EventHandler<T> {
    fun handle(event: T): Mono<Unit>
}