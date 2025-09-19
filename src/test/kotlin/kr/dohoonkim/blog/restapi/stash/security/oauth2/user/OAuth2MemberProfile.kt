package kr.dohoonkim.blog.restapi.stash.security.oauth2.user

import kr.dohoonkim.blog.restapi.security.oauth2.OAuth2Provider
import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2MemberProfile {
    val provider: OAuth2Provider
    val id: String
    val email: String
    val nickname: String
    val profileImage: String
    val emailVerified: Boolean
    val customAttributes: MutableMap<String, Any>
}