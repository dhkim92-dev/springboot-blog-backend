package kr.dohoonkim.blog.restapi.units.common.utility

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.support.entity.createMember
import kr.dohoonkim.blog.restapi.support.security.createJwtAuthentication
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

internal class AuthenticationUtilTest: AnnotationSpec() {

    private lateinit var authenticationUtil: AuthenticationUtil
    private lateinit var memberAuthentication: JwtAuthentication
    private lateinit var adminAuthentication: JwtAuthentication
    @BeforeEach
    fun setUp() {
        val securityContext = mockk<SecurityContext>()
        memberAuthentication = createJwtAuthentication( createMember(role = Role.MEMBER) )
        adminAuthentication = createJwtAuthentication( createMember(role = Role.ADMIN) )
        mockkStatic(SecurityContextHolder::class)
        authenticationUtil = AuthenticationUtil()
    }

    @Test
    fun `인증 정보가 존재하면 MemberId가 추출된다`() {
        every { SecurityContextHolder.getContext().authentication } returns memberAuthentication as Authentication
        authenticationUtil.extractMemberId() shouldBe memberAuthentication.id
    }

    @Test
    fun `인증 정보가 존재하지 않으면 에러가 발생한다`() {
        every { SecurityContextHolder.getContext().authentication } returns null
        shouldThrowAny {
            authenticationUtil.extractMemberId()
        }
    }

    @Test
    fun `관리자 권한이 있으면 true가 반환된다`() {
        every { SecurityContextHolder.getContext().authentication } returns adminAuthentication as Authentication
        authenticationUtil.isAdmin() shouldBe true
    }

    @Test
    fun `관리자 권한이 없으면 false가 반환된다`() {
        every { SecurityContextHolder.getContext().authentication } returns memberAuthentication as Authentication
        authenticationUtil.isAdmin() shouldBe false
    }

    @Test
    fun `리소스 접근 권한을 만족하면 정상 실행된다`() {
        every { SecurityContextHolder.getContext().authentication } returns memberAuthentication as Authentication

        shouldNotThrowAny {
            authenticationUtil.checkPermission(memberAuthentication.id, memberAuthentication.id)
        }
    }

    @Test
    fun `리소스 접근 권한을 만족하지 않으면 ForbiddenException이 발생한다`() {
        every { SecurityContextHolder.getContext().authentication } returns memberAuthentication as Authentication

        shouldThrow<ForbiddenException> {
            authenticationUtil.checkPermission(memberAuthentication.id, adminAuthentication.id)
        }
    }

    @AfterEach
    fun clear() {
        clearAllMocks()
    }
}