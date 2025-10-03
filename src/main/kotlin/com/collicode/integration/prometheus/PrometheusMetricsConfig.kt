package com.collicode.integration.prometheus

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PrometheusMetricsConfig(
    @Value("\${spring.application.name:unknown-service}")
    private val appName: String,

    @Value("\${spring.profiles.active:default}")
    private val environment: String
) {

    /**
     * Add common tags to every metric (application + environment)
     */
    @Bean
    @ConditionalOnProperty(
        name = ["metrics.prometheus.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun metricsCommonTags(): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry ->
            registry.config().commonTags(
                "application", appName,
                "environment", environment
            )
        }
    }
}
