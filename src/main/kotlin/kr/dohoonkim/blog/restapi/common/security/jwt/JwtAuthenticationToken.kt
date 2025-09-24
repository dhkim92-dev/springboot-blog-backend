package kr.dohoonkim.blog.restapi.common.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val principal: Any?,
    private val credentials: String? = null,
    authorities: Collection<GrantedAuthority> = emptyList()
): AbstractAuthenticationToken(authorities) {

    init {
        super.setAuthenticated(false)
        if ( principal != null ) {
            super.setAuthenticated(true)
        }
    }

    // 인증 요청용 토큰(미인증 상태)을 생성하는 생성자
    constructor(credentials: String) : this(principal = "", credentials = credentials) {
        super.setAuthenticated(false)
    }

    override fun getCredentials(): Any? {
        return credentials
    }

    override fun getPrincipal(): Any? {
        return principal
    }
}