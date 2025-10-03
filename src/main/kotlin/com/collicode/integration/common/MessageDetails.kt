package com.collicode.integration.common

import java.time.LocalDateTime

data class MessageDetails(
    val messageId: String,
    val timestamp: LocalDateTime,
    val correlationId: String,
    val ttlMillis: Long,
    val replyTopic: String
)