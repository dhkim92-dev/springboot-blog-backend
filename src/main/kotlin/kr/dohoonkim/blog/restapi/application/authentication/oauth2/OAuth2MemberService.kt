package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.oauth2.user.OAuth2MemberProfile
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.OAuth2Member
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

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

        var member = memberRepository.findByOAuth2UserId(userId = memberProfile.id)

        // 검색된 회원이 없으면 회원가입 처리
        if(member == null) {
            logger.info(" current oauth2 request join to member join phase.")
            member = Member(
                email = "${memberProfile.provider}:${memberProfile.nickname}@dohoon-kim.kr",
                nickname = "${memberProfile.provider.providerName}:${memberProfile.nickname}",
                password = passwordEncoder.encode(createRandomString(12)),
                isBlocked = false,
            )
            val newOauth2Member = OAuth2Member(
                provider = memberProfile.provider,
                userId = memberProfile.id,
                email = memberProfile.email,
            )
            member.linkOAuth2Info(newOauth2Member)
        }

        return memberRepository.save(member)
    }

    private fun createRandomString(n: Int): String {
        val charPool : List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..n)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}