package com.collicode.integration.redis.service

import com.collicode.integration.redis.config.CustomRedisProperties
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration
import java.util.concurrent.TimeUnit


class RedisDataProviderImpl(
    private val stringRedisTemplate: StringRedisTemplate,
    private val customRedisProperties: CustomRedisProperties,
) : RedisDataProvider {

    companion object {
        private val logger = LoggerFactory.getLogger(RedisDataProviderImpl::class.java)
    }

    override fun setString(key: String, value: String, ttl: Duration?): Boolean {
        return try {
            if (ttl != null) {
                stringRedisTemplate.opsForValue().set(key, value, ttl)
            } else {
                stringRedisTemplate.opsForValue().set(key, value)
                stringRedisTemplate.expire(key, Duration.ofSeconds(customRedisProperties.defaultTtlSeconds))
            }
            true
        } catch (e: Exception) {
            logger.error("Failed to set string for key: $key", e)
            false
        }
    }

    override fun getString(key: String): String? {
        return try {
            stringRedisTemplate.opsForValue().get(key)
        } catch (e: Exception) {
            logger.error("Failed to get string for key: $key", e)
            null
        }
    }

    override fun deleteString(key: String): Boolean {
        return deleteKey(key)
    }

    override fun existsString(key: String): Boolean {
        return exists(key)
    }

    override fun addToList(key: String, values: List<String>, ttl: Duration?): Long {
        return try {
            val count = stringRedisTemplate.opsForList().rightPushAll(key, *values.toTypedArray()) ?: 0L
            if (ttl != null) {
                stringRedisTemplate.expire(key, ttl)
            } else {
                stringRedisTemplate.expire(key, Duration.ofSeconds(customRedisProperties.defaultTtlSeconds))
            }
            count
        } catch (e: Exception) {
            logger.error("Failed to add to list for key: $key", e)
            0L
        }
    }

    override fun getList(key: String): List<String> {
        return getListRange(key, 0, -1)
    }

    override fun getListRange(key: String, start: Long, end: Long): List<String> {
        return try {
            stringRedisTemplate.opsForList().range(key, start, end) ?: emptyList()
        } catch (e: Exception) {
            logger.error("Failed to get list range for key: $key", e)
            emptyList()
        }
    }

    override fun removeFromList(key: String, count: Long, value: String): Long {
        return try {
            stringRedisTemplate.opsForList().remove(key, count, value) ?: 0L
        } catch (e: Exception) {
            logger.error("Failed to remove from list for key: $key", e)
            0L
        }
    }

    override fun getListSize(key: String): Long {
        return try {
            stringRedisTemplate.opsForList().size(key) ?: 0L
        } catch (e: Exception) {
            logger.error("Failed to get list size for key: $key", e)
            0L
        }
    }

    override fun deleteList(key: String): Boolean {
        return deleteKey(key)
    }

    override fun addToSet(key: String, values: Set<String>, ttl: Duration?): Long {
        return try {
            val count = stringRedisTemplate.opsForSet().add(key, *values.toTypedArray()) ?: 0L
            if (ttl != null) {
                stringRedisTemplate.expire(key, ttl)
            } else {
                stringRedisTemplate.expire(key, Duration.ofSeconds(customRedisProperties.defaultTtlSeconds))
            }
            count
        } catch (e: Exception) {
            logger.error("Failed to add to set for key: $key", e)
            0L
        }
    }

    override fun getSet(key: String): Set<String> {
        return try {
            stringRedisTemplate.opsForSet().members(key) ?: emptySet()
        } catch (e: Exception) {
            logger.error("Failed to get set for key: $key", e)
            emptySet()
        }
    }

    override fun removeFromSet(key: String, values: Set<String>): Long {
        return try {
            stringRedisTemplate.opsForSet().remove(key, *values.toTypedArray()) ?: 0L
        } catch (e: Exception) {
            logger.error("Failed to remove from set for key: $key", e)
            0L
        }
    }

    override fun isSetMember(key: String, value: String): Boolean {
        return try {
            stringRedisTemplate.opsForSet().isMember(key, value) ?: false
        } catch (e: Exception) {
            logger.error("Failed to check set membership for key: $key", e)
            false
        }
    }

    override fun getSetSize(key: String): Long {
        return try {
            stringRedisTemplate.opsForSet().size(key) ?: 0L
        } catch (e: Exception) {
            logger.error("Failed to get set size for key: $key", e)
            0L
        }
    }

    override fun deleteSet(key: String): Boolean {
        return deleteKey(key)
    }

    override fun setExpire(key: String, ttl: Duration): Boolean {
        return try {
            stringRedisTemplate.expire(key, ttl) ?: false
        } catch (e: Exception) {
            logger.error("Failed to set expiration for key: $key", e)
            false
        }
    }

    override fun getTtl(key: String): Long {
        return try {
            stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)
        } catch (e: Exception) {
            logger.error("Failed to get TTL for key: $key", e)
            -1L
        }
    }

    override fun deleteKey(key: String): Boolean {
        return try {
            stringRedisTemplate.delete(key)
        } catch (e: Exception) {
            logger.error("Failed to delete key: $key", e)
            false
        }
    }

    override fun exists(key: String): Boolean {
        return try {
            stringRedisTemplate.hasKey(key)
        } catch (e: Exception) {
            logger.error("Failed to check existence for key: $key", e)
            false
        }
    }
}
