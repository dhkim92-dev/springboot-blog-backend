package kr.dohoonkim.blog.restapi.security.handler

import com.auth0.jwt.exceptions.JWTVerificationException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.filter.OncePerRequestFilter

class JwtExceptionHandlingFilter(
    private val entryPointUnauthorizedHandler: EntryPointUnauthorizedHandler,
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch(e: AuthenticationException) {
            entryPointUnauthorizedHandler.commence(request, response, e)
        }
    }
}