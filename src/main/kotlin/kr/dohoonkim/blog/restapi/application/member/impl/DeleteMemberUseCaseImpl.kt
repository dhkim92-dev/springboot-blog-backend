package kr.dohoonkim.blog.restapi.application.member.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.member.usecases.DeleteMemberUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DeleteMemberUseCaseImpl(
    private val memberRepository: MemberRepository
) : DeleteMemberUseCase {

    /**
     * 회원 탈퇴
     * @param memberId 요청을 보낸 회원 ID
     * @param targetId 탈퇴 대상 회원 ID
     */
    @Transactional
    override fun deleteMember(memberId: UUID, targetId: UUID) {
        val requester = memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        val targetMember = memberRepository.findByIdOrNull(targetId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        targetMember.withdrawMember(requester)
        memberRepository.save(targetMember)
    }
}