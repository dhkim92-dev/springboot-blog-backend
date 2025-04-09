package kr.dohoonkim.blog.restapi.security.oauth2.user

interface MemberProfileFactory {

    fun build(attributes: MutableMap<String, Any>): OAuth2MemberProfile
}