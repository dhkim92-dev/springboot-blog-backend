package kr.dohoonkim.blog.restapi.units.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.config.RedisConfig
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

class RedisConfigTest: AnnotationSpec() {

    private val redisConfig = RedisConfig(
        host = "127.0.0.1",
        port = 6379,
        objectMapper = jacksonObjectMapper()
    )

    @Test
    fun `redisConnectionFactory를 호출하면 LettuceConnectionFactory가 반환된다`() {
        val factory = redisConfig.redisConnectionFactory()
        (factory is LettuceConnectionFactory) shouldBe true
    }

    @Test
    fun `redisTemplate() 을 호출하면 RedisTemplate_String_Any 객체가 반환된다`() {
        val template = redisConfig.redisTemplate()
        (template is RedisTemplate<String, Any>) shouldBe true
    }

}