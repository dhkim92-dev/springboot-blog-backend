package kr.dohoonkim.blog.restapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kr.dohoonkim.blog.restapi.common.constants.CacheKey
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class CacheConfig(private val redisConnectionFactory : RedisConnectionFactory) {

    private fun defaultCacheConfiguration() : RedisCacheConfiguration {
        val objectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Any::class.java).build(), ObjectMapper.DefaultTyping.EVERYTHING)

        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(3600L))
            .disableCachingNullValues()
            .serializeKeysWith(fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper)))
    }

    private fun customConfigurationMap() : HashMap<String, RedisCacheConfiguration>{
        return hashMapOf<String, RedisCacheConfiguration> (
            CacheKey.CATEGORIES_CACHE_KEY to defaultCacheConfiguration().entryTtl(Duration.ZERO),
        )
    }

    @Bean
    fun cacheManager() : RedisCacheManager {
        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfiguration())
            .withInitialCacheConfigurations(customConfigurationMap())
            .build()
    }
}