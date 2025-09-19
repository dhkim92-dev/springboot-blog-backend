package kr.dohoonkim.blog.restapi.application.member.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.member.dto.MemberCommandModelDto
import kr.dohoonkim.blog.restapi.application.member.dto.UpdateMemberCommand
import kr.dohoonkim.blog.restapi.application.member.usecases.UpdateMemberUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateMemberUseCaseImpl(
    private val memberRepository: MemberRepository
): UpdateMemberUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun update(memberId: UUID, command: UpdateMemberCommand) {
        val updater = memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        val targetMember = memberRepository.findByIdOrNull(command.targetId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        targetMember.updateNickname(updater, command.nickname)
        memberRepository.save(targetMember)
    }
}