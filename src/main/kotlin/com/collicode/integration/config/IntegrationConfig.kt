package com.collicode.integration.config

import com.collicode.integration.kafka.config.KafkaConfig
import com.collicode.integration.kafka.service.KafkaProducerProvider
import com.collicode.integration.kafka.service.impl.KafkaProducerProviderImpl
import com.collicode.integration.prometheus.PrometheusMetricsConfig
import com.collicode.integration.redis.config.CustomRedisProperties
import com.collicode.integration.redis.config.RedisConfig
import com.collicode.integration.redis.service.RedisDataProvider
import com.collicode.integration.redis.service.RedisDataProviderImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.kafka.core.KafkaTemplate


@Configuration
@Import(
    KafkaConfig::class,
    RedisConfig::class,
    PrometheusMetricsConfig::class
)
class IntegrationConfig {


    @Bean
    @ConditionalOnProperty(name = ["spring.kafka.enabled"], havingValue = "true", matchIfMissing = false)
    fun kafkaService(kafkaTemplate: KafkaTemplate<String, String>): KafkaProducerProvider {
        return KafkaProducerProviderImpl(kafkaTemplate)
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.data.redis.enabled"], havingValue = "true", matchIfMissing = false)
    fun redisProvider(
        stringRedisTemplate: StringRedisTemplate,
        customRedisProperties: CustomRedisProperties,
    ): RedisDataProvider {
        return RedisDataProviderImpl(stringRedisTemplate, customRedisProperties)
    }
}
