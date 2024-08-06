package kr.dohoonkim.blog.restapi.security.handler

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class EntryPointUnauthorizedHandler(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
//        log.error("unauthorized exception : {}", request.getAttribute("exception"))
        response.status = HttpStatus.UNAUTHORIZED.value()
//        response.addHeader("Content-Type", "application/json")
        response.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
        response.writer.write(
            objectMapper.writeValueAsString(
                ErrorResponse.of(
                    401,
                    "인증 실패: ${authException.message}",
                    ErrorCodes.AUTHENTICATION_FAIL.code
                )
            )
        )
        response.writer.flush()
        response.writer.close()
    }
}