package kr.dohoonkim.blog.restapi.security.oauth2.revoke

interface OAuth2UserRevoke {

    fun revoke(accessToken: String): Unit
}