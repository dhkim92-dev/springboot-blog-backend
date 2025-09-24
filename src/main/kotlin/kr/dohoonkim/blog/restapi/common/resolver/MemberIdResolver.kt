package kr.dohoonkim.blog.restapi.common.resolver

//import com.nimbusds.oauth2.sdk.auth.JWTAuthentication
import kr.dohoonkim.blog.restapi.common.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.security.LoginInfo
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.UUID

/**
 * class MemberIdResolver
 * @property authenticationUtil
 * SecurityContext에 저장된 AuthenticationPrincipal에서 MemberId를 추출하여 반환
 */
@Component
class MemberIdResolver(
//    private val authenticationUtil: AuthenticationUtil
) : HandlerMethodArgumentResolver {

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UUID {
        return (SecurityContextHolder.getContext().authentication.principal as LoginInfo).memberId
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(MemberId::class.java)
    }
}