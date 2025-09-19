package kr.dohoonkim.blog.restapi.stash.security.oauth2.revoke

interface OAuth2UserRevoke {

    fun revoke(accessToken: String): Unit
}