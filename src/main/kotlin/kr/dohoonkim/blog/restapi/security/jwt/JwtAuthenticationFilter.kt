package kr.dohoonkim.blog.restapi.security.jwt

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.FilterChain

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.EXPIRED_TOKEN_EXCEPTION
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.INVALID_JWT_EXCEPTION
import kr.dohoonkim.blog.restapi.common.error.exceptions.ExpiredTokenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
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
        val token = resolveAuthorizationHeader(request)?.let{ headerValue-> resolveJWTToken(headerValue) }

        if (token == null || SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val authenticationToken = JwtAuthenticationToken(token!!)
            val authentication = jwtAuthenticationProvider.authenticate(authenticationToken)
            authentication.isAuthenticated = true
            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)
        } catch(e: TokenExpiredException) {
            // 만료된 토큰인 경우 재발급 요청을 위해 에러코드를 반환해야한다
            SecurityContextHolder.clearContext()
            throw JwtAuthenticationException(EXPIRED_TOKEN_EXCEPTION)
        } catch(e: Exception) {
            SecurityContextHolder.clearContext()
            throw JwtAuthenticationException(INVALID_JWT_EXCEPTION)
        }
    }

    private fun resolveJWTToken(headerValue: String): String? {
        return if(headerValue.startsWith("Bearer"))
            headerValue.substring(7)
        else null
    }

    private fun resolveAuthorizationHeader(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }
}