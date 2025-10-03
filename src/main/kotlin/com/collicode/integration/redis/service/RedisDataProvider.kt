package com.collicode.integration.redis.service

import java.time.Duration

// Redis data operations interface
interface RedisDataProvider {
    // String operations
    fun setString(key: String, value: String, ttl: Duration? = null): Boolean
    fun getString(key: String): String?
    fun deleteString(key: String): Boolean
    fun existsString(key: String): Boolean

    // List operations
    fun addToList(key: String, values: List<String>, ttl: Duration? = null): Long
    fun getList(key: String): List<String>
    fun getListRange(key: String, start: Long = 0, end: Long = -1): List<String>
    fun removeFromList(key: String, count: Long, value: String): Long
    fun getListSize(key: String): Long
    fun deleteList(key: String): Boolean

    // Set operations
    fun addToSet(key: String, values: Set<String>, ttl: Duration? = null): Long
    fun getSet(key: String): Set<String>
    fun removeFromSet(key: String, values: Set<String>): Long
    fun isSetMember(key: String, value: String): Boolean
    fun getSetSize(key: String): Long
    fun deleteSet(key: String): Boolean

    // Generic operations
    fun setExpire(key: String, ttl: Duration): Boolean
    fun getTtl(key: String): Long
    fun deleteKey(key: String): Boolean
    fun exists(key: String): Boolean
}