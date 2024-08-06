package kr.dohoonkim.blog.restapi.units.security.jwt

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.common.error.exceptions.ExpiredTokenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.JwtInvalidException
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createExpiredAccessToken
import kr.dohoonkim.blog.restapi.support.security.createJwtClaims
import kr.dohoonkim.blog.restapi.support.security.createJwtConfig
import org.springframework.mock.web.MockHttpServletRequest

internal class JwtServiceTest: AnnotationSpec() {

    private val jwtConfig = createJwtConfig(10000000, 600000000)
    private val jwtService = JwtService(jwtConfig)
    private lateinit var jwtClaims: JwtClaims

    override fun isolationMode(): IsolationMode? {
        return IsolationMode.SingleInstance
    }

    @BeforeEach
    fun setUp() {
        val member = createMember(1)[0]
        jwtClaims = createJwtClaims(member)
    }

    @Test
    fun `JwtClaim을 가지고 Access token이 만들어진다`() {
        shouldNotThrowAny {
            val token = jwtService.createAccessToken(jwtClaims)
            token.length shouldBeGreaterThanOrEqual 1
        }
    }

    @Test
    fun `JwtClaim을 Refresh token이 만들어진다`() {
        shouldNotThrowAny {
            val token = jwtService.createRefreshToken(jwtClaims)
            token.length shouldBeGreaterThanOrEqual  1
        }
    }

    @Test
    fun `Access token을 검증하면 JwtClaim과 동일한 값이 나온다`() {
        val token = jwtService.createAccessToken(jwtClaims)
        shouldNotThrowAny {
            val decoded = jwtService.verifyAccessToken(token)
            decoded.issuer shouldBe jwtConfig.issuer
            decoded.audience[0] shouldBe jwtConfig.audience
            decoded.subject shouldBe jwtClaims.id.toString()
            decoded.getClaim("email").asString() shouldBe jwtClaims.email
            decoded.getClaim("nickname").asString() shouldBe jwtClaims.nickname
            decoded.getClaim("roles")
                ?.asArray(String::class.java)
                ?.forEach{ jwtClaims.roles.contains(it) shouldBe true }
            decoded.getClaim("isActivated").asBoolean() shouldBe jwtClaims.isActivated
        }
    }

    @Test
    fun `Refresh token을 검증하면 JwtClaim과 동일한 값이 나온다`() {
        val token = jwtService.createRefreshToken(jwtClaims)
        shouldNotThrowAny {
            val decoded =jwtService.verifyRefreshToken(token)
            decoded.issuer shouldBe jwtConfig.issuer
            decoded.audience[0] shouldBe jwtConfig.audience
            decoded.subject shouldBe jwtClaims.id.toString()
        }
    }

    @Test
    fun `Header에 Bearer이 포함되어 있을 경우 토큰을 추출한다`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer 12345")
        val token = jwtService.extractBearerTokenFromHeader(request)
        token shouldBe "12345"
    }

    @Test
    fun `Authorization Header가 존재하지 않으면 null을 반환한다`() {
        val request = MockHttpServletRequest()
        val token = jwtService.extractBearerTokenFromHeader(request)
        token shouldBe null
    }

    @Test
    fun `Authorization Header가 Bearer로 시작하지 않으면 존재하지 않으면 null을 반환한다`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "abcd")
        val token = jwtService.extractBearerTokenFromHeader(request)
        token shouldBe null
    }

    @Test
    fun `Access Token을 입력하면 JwtAuthentication이 반환된다`() {
        val token = jwtService.createAccessToken(jwtClaims)
        val authentication = jwtService.getAuthentication(token)

        (authentication is JwtAuthentication) shouldBe true
        authentication.id shouldBe jwtClaims.id
        authentication.email shouldBe jwtClaims.email
        authentication.isActivated shouldBe jwtClaims.isActivated
        authentication.nickname shouldBe jwtClaims.nickname
        authentication.roles.forEach{ jwtClaims.roles.contains(it.toString()) shouldBe true }
    }

    @Test
    fun `만료된 Access Token을 입력하면 ExpiredTokenException 이 발생한다`() {
        val expiredToken = createExpiredAccessToken(jwtClaims)

        shouldThrow<ExpiredTokenException> {
            jwtService.getAuthentication(expiredToken)
        }
    }

    @Test
    fun `비정상적인 Access Token을 입력하면 JwtInvalidException이 발생한다`() {
        val invalidToken = "123456"

        shouldThrow<JwtInvalidException> {
            jwtService.getAuthentication(invalidToken)
        }
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}