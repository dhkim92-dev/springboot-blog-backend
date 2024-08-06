package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.vo.MemberProfile
import kr.dohoonkim.blog.restapi.application.authentication.exceptions.NotSupportedOAuth2ProviderException
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.MEMBER_NOT_FOUND
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val delegate: DefaultOAuth2UserService
)
: OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = delegate.loadUser(userRequest)
        val attributes = oAuth2User.attributes
        val provider = userRequest.clientRegistration.registrationId
        val factory = createMemberProfileFactory(provider)
        val memberProfile = factory.build(attributes);
        checkMemberProfile(memberProfile)

        return memberProfile
    }

    @Transactional
    private fun checkMemberProfile(memberProfile: MemberProfile) {
        if (!memberRepository.existsByEmail(memberProfile.email)) {
            val newMember = registerMember(memberProfile)
            memberProfile.nickname = newMember.nickname
        } else {
            memberRepository.findByEmail(memberProfile.email)?.
            let {
                memberProfile.customAuthorities = mutableListOf(SimpleGrantedAuthority(it.role.rolename))
                memberProfile.nickname = it.nickname;
            }
        }
    }

    @Transactional
    private fun registerMember(memberProfile: MemberProfile): Member {
        val email = memberProfile.email
        var nickname = memberProfile.nickname

        if (this.memberRepository.existsByNickname(nickname)) {
            nickname = "user:${UUID.randomUUID().toString()}"
        }

        val member = memberRepository.save(
            Member(
                nickname = nickname,
                email = email,
                password = passwordEncoder.encode(UUID.randomUUID().toString()),
                isActivated = true,
            )
        )

        return member;
    }

    private fun createMemberProfileFactory(provider: String): MemberProfileFactory {
        return when (provider.lowercase()) {
//            "google" -> GoogleMemberProfileFactory();
            "github" -> GithubMemberProfileFactory();
            else -> throw NotSupportedOAuth2ProviderException()
        }
    }
}