package kr.dohoonkim.blog.restapi.units.config

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.config.CacheConfig
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory

internal class CacheConfigTest: AnnotationSpec() {

    private lateinit var cacheConfig: CacheConfig

    @BeforeEach
    fun setUp() {
        val redisConnectionFactory = mockk<RedisConnectionFactory>()
        cacheConfig = CacheConfig((redisConnectionFactory))
    }

    @Test
    fun `cacheManager를 호출하면 RedisCacheManager가 반환된다`() {
        (cacheConfig.cacheManager() is RedisCacheManager) shouldBe true
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}