package kr.dohoonkim.blog.restapi.security.oauth2.revoke

import kr.dohoonkim.blog.restapi.security.oauth2.OAuth2Provider
import kr.dohoonkim.blog.restapi.security.oauth2.exceptions.NotSupportedOAuth2ProviderException
import org.springframework.stereotype.Component

@Component
class OAuth2RevokeManager(
    private val githubRevoke: GithubOAuth2UserRevoke
){

    fun revoke(provider: OAuth2Provider, accessToken: String) {
        return when(provider) {
            OAuth2Provider.GITHUB -> githubRevoke.revoke(accessToken)
            else -> throw NotSupportedOAuth2ProviderException()
        }
    }
}