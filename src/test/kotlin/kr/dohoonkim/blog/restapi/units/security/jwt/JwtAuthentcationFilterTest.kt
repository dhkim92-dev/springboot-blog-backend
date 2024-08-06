package kr.dohoonkim.blog.restapi.units.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kr.dohoonkim.blog.restapi.security.jwt.*
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createJwtClaims
import kr.dohoonkim.blog.restapi.support.security.createNormalAccessToken
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser


internal class JwtAuthentcationFilterTest: AnnotationSpec() {

    private lateinit var jwtService: JwtService
    private lateinit var jwtAuthenticationProvider: JwtAuthenticationProvider
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @BeforeEach
    fun setUp() {
        jwtService = mockk(relaxed = true)
        jwtAuthenticationProvider = mockk(relaxed = true)
        jwtAuthenticationFilter = JwtAuthenticationFilter(jwtService, jwtAuthenticationProvider)
        mockkStatic(SecurityContextHolder::class)
    }

    @Test
    fun `인증 헤더가 존재하지 않으면 인증 정보가 저장되지 않고 다음 필터가 수행된다`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        every {jwtService.extractBearerTokenFromHeader(request)} returns null
        jwtAuthenticationFilter.doFilter(request, response, filterChain)
        every{SecurityContextHolder.getContext().authentication} returns null


        SecurityContextHolder.getContext().authentication shouldBe  null
    }

    @Test
    @WithMockUser
    fun `인증 헤더에 정상적인 token이 존재하고 인증 정보가 없으면 필터가 수행되고 인증 정보가 저장된다`() {
        val jwtClaims = createJwtClaims(createMember(1).get(0))
        val accessToken = createNormalAccessToken(jwtClaims)
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        val jwtAuthentication = JwtAuthentication(id= jwtClaims.id,
            nickname = jwtClaims.nickname,
            email = jwtClaims.email,
            roles = jwtClaims.roles.map{it->SimpleGrantedAuthority(it)}.toMutableList(),
            isActivated = jwtClaims.isActivated
        )

        every { jwtService.extractBearerTokenFromHeader(request) } returns accessToken
        every {jwtAuthenticationProvider.authenticate(any()) } returns jwtAuthentication
        every {SecurityContextHolder.getContext().authentication } returns jwtAuthentication
        jwtAuthenticationFilter.doFilter(request, response, filterChain)
        SecurityContextHolder.getContext().authentication shouldBe jwtAuthentication
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}