package kr.dohoonkim.blog.restapi.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException
    ) {
        log.error("access denied exception : {}", accessDeniedException.message)
        response.status = HttpStatus.FORBIDDEN.value()
        response.addHeader("Content-Type", "application/json;charset=UTF-8")
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCodes.NO_PERMISSION)))
        response.writer.flush()
        response.writer.close()
    }
}