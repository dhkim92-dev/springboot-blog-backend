package kr.dohoonkim.blog.restapi.units.security.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.vo.MemberProfile
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.security.handler.CustomOAuth2AuthenticationSuccessHandler
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createJwtAuthentication
import kr.dohoonkim.blog.restapi.support.security.createJwtConfig
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

internal class CustomOAuth2AuthenticationSuccessHandlerTest: AnnotationSpec() {

    private lateinit var handler: CustomOAuth2AuthenticationSuccessHandler
    private lateinit var memberProfile: MemberProfile
    private lateinit var customUserDetailService: CustomUserDetailService
    private lateinit var memberRepository: MemberRepository
    private lateinit var userDetails: CustomUserDetails
    private val jwtService = JwtService(createJwtConfig(10000, 10000))
    private val objectMapper = jacksonObjectMapper()
    @BeforeEach
    fun setUp() {
        val member = createMember(1)[0]
        memberProfile = MemberProfile(mutableMapOf())
        memberProfile.nickname = member.nickname
        memberProfile.email = member.email
        memberProfile.customAuthorities = mutableListOf(SimpleGrantedAuthority(member.role.rolename))
//        customUserDetailService = mockk()
        memberRepository = mockk()
        userDetails = CustomUserDetails.from(member)
//        every { customUserDetailService.loadUserByUsername(member.email) } returns userDetails
        every { memberRepository.findByEmail(any()) } returns member
        handler = CustomOAuth2AuthenticationSuccessHandler(jwtService, memberRepository, objectMapper)
    }

    @Test
    fun `OAuth2 인증 성공 시 ApiResult로 래핑된 LoginResult 객체가 응답으로 나간다`() {
        val authentication = mockk<Authentication>()
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()

        every {authentication.principal} returns memberProfile

        handler.onAuthenticationSuccess(request, response, authentication)

        response.status shouldBe HttpStatus.CREATED.value()
    }
}