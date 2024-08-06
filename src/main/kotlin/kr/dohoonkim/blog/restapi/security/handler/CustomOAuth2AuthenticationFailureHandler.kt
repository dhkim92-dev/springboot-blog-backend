package kr.dohoonkim.blog.restapi.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.common.contenttype.ContentType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomOAuth2AuthenticationFailureHandler(private val objectMapper: ObjectMapper) :
    SimpleUrlAuthenticationFailureHandler() {

    val logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.status = ErrorCodes.AUTHENTICATION_FAIL.status.value()
        response.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString())
        response.writer.write(
            objectMapper.writeValueAsString(
                ErrorResponse.of(ErrorCodes.AUTHENTICATION_FAIL, "OAuth2 로그인 실패")
            )
        )
        response.writer.flush()
    }
}