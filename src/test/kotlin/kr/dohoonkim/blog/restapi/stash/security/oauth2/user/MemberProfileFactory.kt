package kr.dohoonkim.blog.restapi.stash.security.oauth2.user

interface MemberProfileFactory {

    fun build(attributes: MutableMap<String, Any>): OAuth2MemberProfile
}