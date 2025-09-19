package kr.dohoonkim.blog.restapi.common.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val authenticationManager: AuthenticationManager
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractHeader(request)

        if ( token == null ) {
            filterChain.doFilter(request, response)
            return;
        }
        val jwtAuthenticationToken = JwtAuthenticationToken(principal = null, credentials = token)
        val authentication = authenticationManager.authenticate(jwtAuthenticationToken)
        if ( authentication.isAuthenticated ) {
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun extractHeader(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")

        if (header.isNullOrBlank() || !header.startsWith("Bearer ")) {
            return null
        }

        return header.substring(7)
    }
}