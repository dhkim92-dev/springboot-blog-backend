package kr.dohoonkim.blog.restapi.security.oauth2

import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.OAuth2Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.domain.member.repository.OAuth2MemberRepository
import kr.dohoonkim.blog.restapi.security.oauth2.revoke.OAuth2RevokeManager
import kr.dohoonkim.blog.restapi.security.oauth2.user.OAuth2MemberProfile
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OAuth2MemberService(
    private val memberRepository: MemberRepository,
    private val oAuth2MemberRepository: OAuth2MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val revokeManager: OAuth2RevokeManager
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun getOrJoin(memberProfile: OAuth2MemberProfile, accessToken: String): Member {
        logger.info("""OAuth2 sign in request
            provider: ${memberProfile.provider} 
            userId:${memberProfile.id} 
            nickname: ${memberProfile.nickname}
            email: ${memberProfile.email}
            emailVerified: ${memberProfile.emailVerified}
        """.trimMargin())
        val oauth2Member = oAuth2MemberRepository.findByProviderAndUserId(memberProfile.provider, memberProfile.id)

        if(oauth2Member == null) {
            logger.info(" current oauth2 request join to member join phase.")
            val member = this.memberRepository.save(Member(
                email = "${memberProfile.provider}:${memberProfile.nickname}@dohoon-kim.kr",
                nickname = "${memberProfile.provider.providerName}:${memberProfile.nickname}",
                password = passwordEncoder.encode(UUID.randomUUID().toString()),
                isActivated = true
            ))
            var newOauth2Member = OAuth2Member(
                provider = memberProfile.provider,
                userId = memberProfile.id,
                email = memberProfile.email,
            )
            newOauth2Member.member = member
            newOauth2Member.accessToken = accessToken
            return oAuth2MemberRepository.save(newOauth2Member).member
        }
        oauth2Member.accessToken = accessToken
        return oAuth2MemberRepository.save(oauth2Member).member
    }

    @Transactional
    fun revoke(memberProfile: OAuth2MemberProfile, accessToken: String) {
        revokeManager.revoke(memberProfile.provider, accessToken)
        oAuth2MemberRepository.findByProviderAndUserId(memberProfile.provider, memberProfile.id)
            ?.let{
                oAuth2Member ->
                if(oAuth2Member.member.email.startsWith(memberProfile.provider.providerName)) {
                    memberRepository.delete(oAuth2Member.member)
                } else {
                    oAuth2MemberRepository.delete(oAuth2Member)
                }
            }

    }
}