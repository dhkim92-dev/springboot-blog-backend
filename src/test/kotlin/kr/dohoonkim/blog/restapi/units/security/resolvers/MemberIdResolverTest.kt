package kr.dohoonkim.blog.restapi.units.security.resolvers

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.security.annotations.MemberId
import kr.dohoonkim.blog.restapi.security.resolver.MemberIdResolver
import kr.dohoonkim.blog.restapi.support.mockMethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.NativeWebRequest
import java.util.*

internal class MemberIdResolverTest: AnnotationSpec() {

    private lateinit var authenticationUtil: AuthenticationUtil
    private lateinit var memberIdResolver: MemberIdResolver
    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerLeaf

    @BeforeEach
    fun setUp() {
        authenticationUtil = mockk()
        memberIdResolver = MemberIdResolver(authenticationUtil)
//        SecurityContextHolder.clearContext()
    }

    @Test
    fun `메소드 맥변수에 MemberId 어노테이션이 붙어있으면 supportsParameter가 true를 반환한다`() {
        val methodParameter = mockMethodParameter("memberId")
        every {methodParameter.hasParameterAnnotation(MemberId::class.java)} returns true

        memberIdResolver.supportsParameter(methodParameter) shouldBe  true
    }

    @Test
    fun `MemberId 어노테이션이 붙어있지 않으면 supports() 가 false를 반환한다`() {
        val methodParameter = mockMethodParameter("memberId")
        every {methodParameter.hasParameterAnnotation(MemberId::class.java)} returns false

        memberIdResolver.supportsParameter(methodParameter) shouldBe false
    }

    @Test
    fun `인증 정보가 존재할 경우 사용자 ID가 반환된다`() {
        val memberId = UUID.randomUUID()
        every { authenticationUtil.extractMemberId() } returns memberId
        val parameter = mockMethodParameter("memberId")
        val webRequest = mockk<NativeWebRequest>()
        memberIdResolver.resolveArgument(parameter, null, webRequest, null) shouldBe memberId
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}