package kr.dohoonkim.blog.restapi.application.member.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.member.dto.CreateMemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.application.member.usecases.CreateMemberUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * MemberCreateUsecase 구현 객체
 */
@Service
class CreateMemberUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) : CreateMemberUseCase {

    @Transactional
    override fun createMember(command: CreateMemberCommand): MemberDto {
        if ( memberRepository.existsByEmail(command.email) ) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_EMAIL)
        }

        if (memberRepository.existsByNickname(command.nickname) ) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_NICKNAME)
        }

        val member = Member(
            email = command.email,
            nickname = command.nickname,
            password = passwordEncoder.encode(command.password)
        )

        return MemberDto.from(memberRepository.save(member))
    }
}