package com.collicode.integration.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.kafka") // <-- Required
open class KafkaProperties {
    var bootstrapServers: List<String> = listOf()
    var clientId: String = "my-app-client"
    var defaultTopic: String = "my-default-topic"
    var consumer: Consumer = Consumer()
    var producer: Producer = Producer()
    var enabled: Boolean = false

    open class Consumer {
        var groupId: String = "my-app-group"
        var autoOffsetReset: String = "earliest"
        var enableAutoCommit: Boolean = false
        var maxPollRecords: Int = 500
        var maxPollIntervalMs: Int = 300_000
        var sessionTimeoutMs: Int = 10_000
        var isolationLevel: String = "read_committed"
    }

    open class Producer {
        var acks: String = "all"
        var retries: Int = 5
        var batchSize: Int = 16384
        var bufferMemory: Long = 33554432
        var compressionType: String = "gzip"
        var lingerMs: Int = 5
    }
}
