package kr.dohoonkim.blog.restapi.security.jwt

import jakarta.servlet.FilterChain

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT 인증 필터
 * @property jwtService Jwt token 생성 및 header 추출 담당 서비스
 * @property jwtAuthenticationProvider 인증 프로바이더
 */
class JwtAuthenticationFilter(
    private val jwtService : JwtService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) : OncePerRequestFilter(){

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 필터 로직, Authorization 헤더에 토큰이 포함되어있다면 이 필터가 수행된다.
     * Endpoint에 따라 필터를 수행하지 않는 로직은 추가하지 않는다. 추후 다른 기능들이 추가하면 그때 고려
     */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token: String? = this.jwtService.extractBearerTokenFromHeader(request)
        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            log.debug("authentication processing.")
            val authenticationToken = JwtAuthenticationToken(token!!)
            val authentication = jwtAuthenticationProvider.authenticate(authenticationToken)
            SecurityContextHolder.getContext().authentication = authentication
        }

        doFilter(request, response, filterChain)
    }

}