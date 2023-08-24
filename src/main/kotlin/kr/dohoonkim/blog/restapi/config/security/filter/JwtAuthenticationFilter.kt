package kr.dohoonkim.blog.restapi.config.security.filter

import jakarta.servlet.FilterChain

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.config.security.jwt.JwtAuthenticationToken
import kr.dohoonkim.blog.restapi.config.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.config.security.provider.JwtAuthenticationProvider
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT 인증 절차
 * 1) 클라이언트는 Authorization Header에 Bearer <Access Token> 형식의 인증 토큰을 보낸다.
 * 2) JwtAuthenticationFilter는 [JwtService] 객체를 이용하여 토큰을 Authorization Header에서 추출한다.
 * 3) token 이 존재하지 않는다면 다음 필터를 수행하고 이 필터를 종료한다.
 * 4) token 이 존재한다면 [JwtAuthenticationToken] 을 생성한다.
 * 5) [JwtAuthenticationToken]을 [JwtAuthenticationProvider.authenticate] 메소드를 통해 인증 절차를 수행한다.
 * 6) 5) 의 과정이 성공했다면 JwtAuthentication 을 생성하여 [SecurityContextHolder] 를 이용하여 인증 정보를 저장한 후 다음 필터로 진행한다.
 */

class JwtAuthenticationFilter(
        private val matcher : RequestMatcher,
        private val jwtService : JwtService,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider) : OncePerRequestFilter(){

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if(matcher.matches(request)) {
            val token: String? = this.jwtService.extractBearerTokenFromHeader(request)

            if (token != null) {
                log.debug("authentication processing.")
                val authenticationToken = JwtAuthenticationToken(token!!)
                val authentication = jwtAuthenticationProvider.authenticate(authenticationToken)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        doFilter(request, response, filterChain)
    }

}