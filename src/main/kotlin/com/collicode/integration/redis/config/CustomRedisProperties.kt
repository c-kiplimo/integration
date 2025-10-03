package com.collicode.integration.redis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.redis")
data class CustomRedisProperties(
    val keyPrefix: String = "taskprovider",
    val defaultTtlSeconds: Long = 3600,
    val enableMetrics: Boolean = true,
    val retryAttempts: Int = 3
)