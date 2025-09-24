package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.oauth2.exceptions.NotSupportedOAuth2ProviderException
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.user.MemberProfileFactory
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.user.github.GithubMemberProfileFactory
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val oauth2MemberFactories: List<MemberProfileFactory>
): DefaultOAuth2UserService() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        var oAuth2User = super.loadUser(userRequest)
        logger.info("success to get oauth2 user info")
        return processingOAuth2User(oAuth2User = oAuth2User, userRequest = userRequest)
    }

    private fun processingOAuth2User(userRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2UserPrincipal {
        val provider = userRequest.clientRegistration.registrationId
        val memberProfileFactory = getMemberProfileFactory(provider)
        val attributes = oAuth2User.attributes
        val memberProfile = memberProfileFactory.build(attributes)
        val accessToken = userRequest.accessToken.tokenValue
        return OAuth2UserPrincipal(memberProfile, accessToken)
    }

    private fun getMemberProfileFactory(provider: String): MemberProfileFactory {
        return oauth2MemberFactories.find { it.supports(provider) }
            ?: throw NotSupportedOAuth2ProviderException()
    }
}
