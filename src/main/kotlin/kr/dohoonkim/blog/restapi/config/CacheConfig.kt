package kr.dohoonkim.blog.restapi.config

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun defaultCacheConfiguration() : RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(600L))
            .disableCachingNullValues()
            .serializeKeysWith(fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(fromSerializer(GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    fun redisCacheManagerBuilderCustomizer() : RedisCacheManagerBuilderCustomizer {
        return RedisCacheManagerBuilderCustomizer {
            builder -> builder
            .withCacheConfiguration("articles", defaultCacheConfiguration().entryTtl(Duration.ZERO))
            .withCacheConfiguration("article-categories", defaultCacheConfiguration().entryTtl(Duration.ZERO))
        }
    }

}