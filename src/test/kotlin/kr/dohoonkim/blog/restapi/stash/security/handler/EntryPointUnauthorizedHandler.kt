package kr.dohoonkim.blog.restapi.stash.security.handler

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.AUTHENTICATION_FAIL
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class EntryPointUnauthorizedHandler(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("EntryPointUnauthorizedHandler called")

        val message = when(authException) {
            is JwtAuthenticationException -> authException.errorCode.message
            else -> AUTHENTICATION_FAIL.message
        }
        val code = when(authException) {
            is JwtAuthenticationException -> authException.errorCode.code
            else -> AUTHENTICATION_FAIL.code
        }

        response.status = HttpStatus.UNAUTHORIZED.value()
//        response.addHeader("Content-Type", "application/json")
        response.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
        response.writer.write(
            objectMapper.writeValueAsString(
                ErrorResponse.of(
                    401,
                    message,
                    code
                )
            )
        )
        response.writer.flush()
        response.writer.close()
    }
}