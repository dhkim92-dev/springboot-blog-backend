package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.oauth2.revoke.OAuth2RevokeManager
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.user.OAuth2MemberProfile
import kr.dohoonkim.blog.restapi.application.member.usecases.CreateMemberUseCase
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.OAuth2Member
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.OAuth2MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OAuth2MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
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

        //val oauth2Member = oAuth2MemberRepository.findByProviderAndUserId(memberProfile.provider, memberProfile.id)
        val member = memberRepository.findByOAuth2Info(
            provider = memberProfile.provider,
            userId = memberProfile.id
        )

        if(member == null) {
            logger.info(" current oauth2 request join to member join phase.")
            val member = this.memberRepository.save(Member(
                email = "${memberProfile.provider}:${memberProfile.nickname}@dohoon-kim.kr",
                nickname = "${memberProfile.provider.providerName}:${memberProfile.nickname}",
                password = passwordEncoder.encode(UUID.randomUUID().toString()),
                isBlocked = false,
            ))
            var newOauth2Member = OAuth2Member(
                provider = memberProfile.provider,
                userId = memberProfile.id,
                email = memberProfile.email,
            )
            newOauth2Member.member = member
            return oAuth2MemberRepository.save(newOauth2Member).member
        }

        return memberRepository.save(member)
    }
}