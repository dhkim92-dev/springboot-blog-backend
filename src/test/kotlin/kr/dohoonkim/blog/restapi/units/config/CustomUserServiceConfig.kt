package kr.dohoonkim.blog.restapi.units.config

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.config.CustomUserServiceConfig
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder

internal class CustomUserServiceConfigTest: AnnotationSpec() {

    private lateinit var memberRepository: MemberRepository
    private lateinit var customUserServiceConfig: CustomUserServiceConfig
    @BeforeEach
    fun setUp() {
        memberRepository = mockk()
        customUserServiceConfig = CustomUserServiceConfig(memberRepository)
    }

    @Test
    fun `passwordEncrypt를 호출하면 PasswordEncoder가 반횐된다`() {
        val ret = customUserServiceConfig.passwordEncrypt()
        (ret is PasswordEncoder) shouldBe true
    }

    @Test
    fun `customUserDetailService를 호출하면 객체가 반환된다`() {
        val ret = customUserServiceConfig.customUserDetailService()
        (ret is CustomUserDetailService) shouldBe true
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}