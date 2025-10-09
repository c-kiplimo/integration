package com.collicode.integration.kafka.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import reactor.kafka.receiver.ReceiverOptions

@Configuration
@ConditionalOnProperty(
    value = ["spring.kafka.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableConfigurationProperties(KafkaProperties::class)
class KafkaConfig(
    private val kafkaProperties: KafkaProperties
) {

    @Bean
    @Primary
    fun producerFactory(): ProducerFactory<String, String> {
        val configProps = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.joinToString(","),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.ACKS_CONFIG to kafkaProperties.producer.acks,
            ProducerConfig.RETRIES_CONFIG to kafkaProperties.producer.retries,
            ProducerConfig.BATCH_SIZE_CONFIG to kafkaProperties.producer.batchSize,
            ProducerConfig.BUFFER_MEMORY_CONFIG to kafkaProperties.producer.bufferMemory,
            ProducerConfig.COMPRESSION_TYPE_CONFIG to kafkaProperties.producer.compressionType,
            ProducerConfig.LINGER_MS_CONFIG to kafkaProperties.producer.lingerMs
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    @Primary
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    @Primary
    fun consumerFactory(): ConsumerFactory<String, String> {
        val configProps = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.joinToString(","),
            ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.consumer.autoOffsetReset,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to kafkaProperties.consumer.enableAutoCommit,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to kafkaProperties.consumer.maxPollRecords,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to kafkaProperties.consumer.maxPollIntervalMs,
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to kafkaProperties.consumer.sessionTimeoutMs,
            ConsumerConfig.ISOLATION_LEVEL_CONFIG to kafkaProperties.consumer.isolationLevel,
            ConsumerConfig.CLIENT_ID_CONFIG to kafkaProperties.clientId
        )

        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    // ✅ ADD THIS - Reactive Kafka Receiver Options
    @Bean
    fun receiverOptions(): ReceiverOptions<String, String> {
        val configProps = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers.joinToString(","),
            ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to kafkaProperties.consumer.autoOffsetReset,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to kafkaProperties.consumer.enableAutoCommit,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to kafkaProperties.consumer.maxPollRecords,
            ConsumerConfig.FETCH_MIN_BYTES_CONFIG to 1,
            ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG to 500
        )

        return ReceiverOptions.create(configProps)
    }
}