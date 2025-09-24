package kr.dohoonkim.blog.restapi.application.authentication.oauth2.user

interface MemberProfileFactory {

    fun supports(provider: String): Boolean

    fun build(attributes: MutableMap<String, Any>): OAuth2MemberProfile
}
