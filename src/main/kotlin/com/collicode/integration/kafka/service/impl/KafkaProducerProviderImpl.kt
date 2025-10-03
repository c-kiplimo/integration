package com.collicode.integration.kafka.service.impl

import com.collicode.integration.common.MessageDetails
import com.collicode.integration.kafka.service.KafkaProducerProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@ConditionalOnProperty(
    value = ["spring.kafka.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Primary
@Service("kafkaProducerProviderImpl")
class KafkaProducerProviderImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>
) : KafkaProducerProvider {

    override fun sendMessage(topicName: String, message: Any, messageKey: String, messageDetails: MessageDetails?) {
        val builder = MessageBuilder
            .withPayload(message)
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .setHeader(KafkaHeaders.TIMESTAMP, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())

        if (messageDetails?.ttlMillis != null) {
            builder.setHeader("ttlMillis", messageDetails.ttlMillis)
            builder.setHeader("expiryTime", System.currentTimeMillis() + messageDetails.ttlMillis)
        }
        if (messageDetails?.timestamp != null) {
            builder.setHeader(KafkaHeaders.TIMESTAMP, messageDetails.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli())
        }

        kafkaTemplate.send(builder.build())
    }

    override fun sendMessage(topicName: String, message: String, messageDetails: MessageDetails?) {
        val builder = MessageBuilder
            .withPayload(message)
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .setHeader(KafkaHeaders.TIMESTAMP, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())

        if (messageDetails?.ttlMillis != null) {
            builder.setHeader("ttlMillis", messageDetails.ttlMillis)
            builder.setHeader("expiryTime", System.currentTimeMillis() + messageDetails.ttlMillis)
        }
        if (messageDetails?.timestamp != null) {
            builder.setHeader(KafkaHeaders.TIMESTAMP, messageDetails.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli())
        }

        kafkaTemplate.send(builder.build())
    }
}