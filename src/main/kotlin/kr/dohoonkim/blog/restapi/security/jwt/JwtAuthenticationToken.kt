package kr.dohoonkim.blog.restapi.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

/**
 * JwtAuthenticationProvider 에서 다룰 JwtAuthenticationToken
 * @property _principal : 인증전 JWT String, 인증 후 JwtAuthentication이 된다
 */

class JwtAuthenticationToken(var _principal : Any)
:  Authentication { //AbstractAuthenticationToken(null) {

    override fun getName(): String {
        return (_principal as JwtAuthentication).nickname
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return (_principal as JwtAuthentication).authorities
    }

    override fun getDetails(): Any {
        return (_principal as JwtAuthentication).id
    }

    override fun isAuthenticated(): Boolean {
        return (_principal as JwtAuthentication).isActivated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {

    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return _principal
    }
}