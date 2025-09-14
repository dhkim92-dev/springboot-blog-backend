package kr.dohoonkim.blog.restapi.security.oauth2

import kr.dohoonkim.blog.restapi.security.oauth2.user.OAuth2MemberProfile
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserPrincipal(
    private val memberProfile: OAuth2MemberProfile,
    private val accessToken: String,
): OAuth2User {

    fun getToken(): String {
        return accessToken
    }

    fun getProfile(): OAuth2MemberProfile {
        return memberProfile
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return memberProfile.customAttributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getName(): String {
        return memberProfile.nickname
    }
}