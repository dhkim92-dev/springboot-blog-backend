package kr.dohoonkim.blog.restapi.stash.security.oauth2.revoke

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.logging.Logger

@Component
class GithubOAuth2UserRevoke(
    @Value("\${spring.security.oauth2.client.registration.github.client-id}")
    private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.github.client-secret}")
    private val clientSecret: String,
    private val webClient: WebClient
): OAuth2UserRevoke {

    private val GITHUB_REVOKE_URL="https://api.github.com/applications/${clientId}/grant"
    private val GITHUB_API_VERSION_HEADER_NAME="X-GitHub-Api-Version"
    private val GITHUB_API_VERSION_HEADER_VALUE = "2022-11-28"
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun revoke(accessToken: String) {
        logger.info("Github access token : ${accessToken} will be revoked.")
        val body = mapOf("access_token" to accessToken)

        webClient
            .method(HttpMethod.DELETE)
            .uri(GITHUB_REVOKE_URL)
            .header(GITHUB_API_VERSION_HEADER_NAME, GITHUB_API_VERSION_HEADER_VALUE)
            .header("Accept", "application/vnd.github+json")
            .headers { headers ->
                headers.setBasicAuth(clientId, clientSecret)
            }
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .flatMap { response ->
                when(response.statusCode) {
                    HttpStatus.NO_CONTENT -> Mono.just(Unit)
                    else -> Mono.error(
                        UnauthorizedException(errorCode = ErrorCodes.OAUTH2_REVOKE_FAILED)
                    )
                }
            }
            .onErrorResume { e -> Mono.error(e) }
            .block()
        logger.info("Github access token : ${accessToken} revoked.")
    }
}