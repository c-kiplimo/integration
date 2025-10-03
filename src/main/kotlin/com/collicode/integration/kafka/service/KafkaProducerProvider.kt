package com.collicode.integration.kafka.service

import com.collicode.integration.common.MessageDetails


interface KafkaProducerProvider {
    fun sendMessage(topicName: String, message: Any, messageKey: String, messageDetails: MessageDetails? = null)
    fun sendMessage(topicName: String, message: String, messageDetails: MessageDetails? = null)

}