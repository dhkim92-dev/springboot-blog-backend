package kr.dohoonkim.blog.restapi.units.security.jwt

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.common.error.exceptions.ExpiredTokenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.JwtInvalidException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createExpiredAccessToken
import kr.dohoonkim.blog.restapi.support.security.createJwtAuthentication
import kr.dohoonkim.blog.restapi.support.security.createJwtConfig
import kr.dohoonkim.blog.restapi.support.security.createNormalAccessToken
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.core.authority.SimpleGrantedAuthority

internal class JwtServiceTest: AnnotationSpec() {

    private val jwtConfig = createJwtConfig(10000000, 600000000)
    private val jwtService = JwtService(jwtConfig)
    private lateinit var member: Member
    private lateinit var jwtAuthentication: JwtAuthentication

    override fun isolationMode(): IsolationMode? {
        return IsolationMode.SingleInstance
    }

    @BeforeEach
    fun setUp() {
        member = createMember(1).first()
        jwtAuthentication = createJwtAuthentication(member)
    }

    @Test
    fun `Member가 주어지면 Access token이 만들어진다`() {
        shouldNotThrowAny {
            val token = jwtService.createAccessToken(member)
            token.length shouldBeGreaterThanOrEqual 1
        }
    }

    @Test
    fun `Member가 주어지면 Refresh token이 만들어진다`() {
        shouldNotThrowAny {
            val token = jwtService.createRefreshToken(member)
            token.length shouldBeGreaterThanOrEqual  1
        }
    }

    @Test
    fun `Access token을 검증하면 Member와 동일한 값이 나온다`() {
        val token = jwtService.createAccessToken(member)
        shouldNotThrowAny {
            val decoded = jwtService.verifyAccessToken(token)
            decoded.issuer shouldBe jwtConfig.issuer
            decoded.audience[0] shouldBe jwtConfig.audience
            decoded.subject shouldBe member.id.toString()
            decoded.getClaim("email").asString() shouldBe member.email
            decoded.getClaim("nickname").asString() shouldBe member.nickname
            decoded.getClaim("roles")
                ?.asArray(String::class.java)
                ?.forEach{ role -> role shouldBe member.role.rolename }
            decoded.getClaim("isActivated").asBoolean() shouldBe member.isActivated
        }
    }

    @Test
    fun `Refresh token을 검증하면 Member ID와 동일한 값이 나온다`() {
        val token = jwtService.createRefreshToken(member)
        shouldNotThrowAny {
            val decoded =jwtService.verifyRefreshToken(token)
            decoded.issuer shouldBe jwtConfig.issuer
            decoded.audience[0] shouldBe jwtConfig.audience
            decoded.subject shouldBe member.id.toString()
        }
    }

    @Test
    fun `Access Token을 입력하면 JwtAuthentication이 반환된다`() {
        val token = jwtService.createAccessToken(member)
        val authentication = jwtService.getAuthentication(token)

        (authentication is JwtAuthentication) shouldBe true
        authentication.id shouldBe member.id
        authentication.email shouldBe member.email
        authentication.isActivated shouldBe member.isActivated
        authentication.nickname shouldBe member.nickname
        authentication.roles.contains(SimpleGrantedAuthority(member.role.rolename)) shouldBe true
    }

    @Test
    fun `만료된 Access Token을 입력하면 TokenExpiredException 이 발생한다`() {
        val expiredToken = createExpiredAccessToken(member)

        shouldThrow<TokenExpiredException> {
            jwtService.getAuthentication(expiredToken)
        }
    }

    @Test
    fun `비정상적인 Access Token을 입력하면 JWTDecodeException이 발생한다`() {
        val invalidToken = "123456"

        shouldThrow<JWTDecodeException> {
            jwtService.getAuthentication(invalidToken)
        }
    }

    @Test
    fun `Access Token을 RefreshToken을 발급받으려고 하면 SignatureVerificationException이 발생한다`() {
        val accessToken = createNormalAccessToken(member)
        shouldThrow<SignatureVerificationException> {
            jwtService.verifyRefreshToken(accessToken)
        }
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}