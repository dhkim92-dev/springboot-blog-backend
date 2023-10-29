package kr.dohoonkim.blog.restapi.units.security.resolvers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.security.resolver.MemberIdResolver
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import java.util.UUID

class MemberIdResolverTest : BehaviorSpec({
    val authenticationUtil = mockk<AuthenticationUtil>()
    val resolver = MemberIdResolver(authenticationUtil)
    val methodParameter = mockk<MethodParameter>()
    val nativeWebRequest = mockk<NativeWebRequest>()

    Given("컨트롤러에 MemberId Annotation을 사용하고") {
        When("사용자 인증 정보가 존재하면") {
            val value = UUID.randomUUID()
            every { authenticationUtil.extractMemberId() } returns value

            Then("UUID가 반환된다.") {
                resolver.resolveArgument(methodParameter, null, nativeWebRequest, null) shouldBe value
            }
        }

        When("사용자 인증 정보가 존재하지 않으면") {
            every { authenticationUtil.extractMemberId() } throws UnauthorizedException()
            Then("에러가 발생한다.") {
                shouldThrow<UnauthorizedException> {
                    resolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
                }
            }
        }
    }
})