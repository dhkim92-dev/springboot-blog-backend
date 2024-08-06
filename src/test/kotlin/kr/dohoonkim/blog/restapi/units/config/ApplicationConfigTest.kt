package kr.dohoonkim.blog.restapi.units.config

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.common.filter.MultiPartSnake2CamelFilter
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.config.ApplicationConfig
import kr.dohoonkim.blog.restapi.security.resolver.MemberIdResolver
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.web.method.support.HandlerMethodArgumentResolver

internal class ApplicationConfigTest: AnnotationSpec() {

    private val authenticationUtil = AuthenticationUtil()
    private val memberIdResolver = MemberIdResolver(authenticationUtil)
    private val applicationConfig = ApplicationConfig(memberIdResolver)

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `addArgumentResolver를 호출하면 memberIdResolver가 추가된다`() {
        val resolvers = mutableListOf<HandlerMethodArgumentResolver>()
        applicationConfig.addArgumentResolvers(resolvers)
        resolvers.contains(memberIdResolver) shouldBe true
    }

    @Test
    fun `jsonCustomizer를 호출하면 Jackson2ObjectMapperBuilderCustomizer가 반환된다`() {
        val ret = applicationConfig.jsonCustomizer()
        (ret is Jackson2ObjectMapperBuilderCustomizer) shouldBe true
    }

    @Test
    fun `multipartSnake2CamelFilter를 호출하면 MultipartSnake2CamelFilter 객체가 반환된다`() {
        val ret = applicationConfig.multipartSnake2CamelFilter()

        (ret is MultiPartSnake2CamelFilter) shouldBe true
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}