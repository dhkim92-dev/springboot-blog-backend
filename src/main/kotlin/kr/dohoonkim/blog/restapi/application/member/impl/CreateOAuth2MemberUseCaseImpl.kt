package kr.dohoonkim.blog.restapi.application.member.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.member.dto.CreateOAuth2MemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.application.member.usecases.CreateOAuth2MemberUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.OAuth2Member
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.OAuth2MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateOAuth2MemberUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val oauth2InfoRepository: OAuth2MemberRepository,
    private val passwordEncoder: PasswordEncoder
): CreateOAuth2MemberUseCase {

    @Transactional
    override fun create(command: CreateOAuth2MemberCommand): MemberDto {
        val oauth2Info = oauth2InfoRepository.findByProviderAndUserId(
            provider = command.provider,
            userId = command.userId
        )

        if ( oauth2Info != null) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_EMAIL)
        }

        if ( memberRepository.existsByEmail(command.email) ) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_EMAIL)
        }

        val member = Member(
            nickname = command.nickname,
            email = command.email,
            password = passwordEncoder.encode(UUID.randomUUID().toString()),
        )

        member.linkOAuth2Info(
            OAuth2Member(
                provider = command.provider,
                userId = command.userId,
            )
        )

        return MemberDto.from(memberRepository.save(member))
    }
}