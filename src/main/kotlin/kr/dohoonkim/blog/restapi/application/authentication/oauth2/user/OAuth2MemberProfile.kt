package kr.dohoonkim.blog.restapi.application.authentication.oauth2.user

import kr.dohoonkim.blog.restapi.domain.member.OAuth2Provider

interface OAuth2MemberProfile {
    val provider: OAuth2Provider
    val id: String
    val email: String
    val nickname: String
    val profileImage: String
    val emailVerified: Boolean
    val customAttributes: MutableMap<String, Any>
}