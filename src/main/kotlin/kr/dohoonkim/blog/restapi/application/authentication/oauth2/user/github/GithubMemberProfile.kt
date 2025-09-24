package kr.dohoonkim.blog.restapi.application.authentication.oauth2.user.github

import kr.dohoonkim.blog.restapi.application.authentication.oauth2.user.OAuth2MemberProfile
import kr.dohoonkim.blog.restapi.domain.member.OAuth2Provider
import org.springframework.security.core.GrantedAuthority

/**
 * Github Member Profile
 * attributes가 제공되면 nickname 과 email을 생성한다
 * @author dhkim92.dev@gmail.com
 * @since 2024.01.10
 */
class GithubMemberProfile(
    override val provider: OAuth2Provider,
    override val id: String,
    override val email: String,
    override val nickname: String,
    override val emailVerified: Boolean,
    override val profileImage: String,
    override val customAttributes: MutableMap<String, Any>
): OAuth2MemberProfile {

}