package kr.dohoonkim.blog.restapi.application.member.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.member.dto.UpdatePasswordCommand
import kr.dohoonkim.blog.restapi.application.member.usecases.UpdatePasswordUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdatePasswordUseCaseImpl(
    private val memberRepository: MemberRepository
): UpdatePasswordUseCase {

    @Transactional
    override fun updatePassword(memberId: UUID, command: UpdatePasswordCommand) {
        val updater = memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        val targetMember = memberRepository.findByIdOrNull(command.targetId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)
    }
}