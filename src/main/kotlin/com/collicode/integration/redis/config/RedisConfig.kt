package com.collicode.integration.redis.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@ConditionalOnProperty(
    value = ["spring.data.redis.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableConfigurationProperties(RedisProperties::class, CustomRedisProperties::class)
class RedisConfig {

    @Bean
    fun redisConnectionFactory(redisProperties: RedisProperties): LettuceConnectionFactory {
        val clientConfig = LettucePoolingClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(5000))
            .poolConfig(genericPoolConfig())
            .build()

        val redisConfig = RedisStandaloneConfiguration().apply {
            hostName = redisProperties.host
            port = redisProperties.port
            password = RedisPassword.of(redisProperties.password)
            database = redisProperties.database

        }

        return LettuceConnectionFactory(redisConfig, clientConfig)
    }

    private fun genericPoolConfig(): GenericObjectPoolConfig<*> {
        return GenericObjectPoolConfig<Any>().apply {
            maxTotal = 50
            maxIdle = 20
            minIdle = 10
            maxWaitMillis = 1000
        }
    }

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            connectionFactory = factory
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            hashKeySerializer = GenericJackson2JsonRedisSerializer(objectMapper())
            hashValueSerializer = GenericJackson2JsonRedisSerializer(objectMapper())
        }
    }

    @Bean
    fun stringRedisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
        val template = StringRedisTemplate()
        template.connectionFactory = connectionFactory
        return template
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
                .registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules()
        }
    }

}
