package kr.dohoonkim.blog.restapi.application.member.impl

import kr.dohoonkim.blog.restapi.application.member.dto.MemberQueryModelDto
import kr.dohoonkim.blog.restapi.application.member.usecases.QueryMemberUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class QueryMemberUseCaseImpl(
    private val memberRepository: MemberRepository
) : QueryMemberUseCase {

    override fun getMember(memberId: UUID, targetId: UUID)
    : MemberQueryModelDto {
        val member = memberRepository.findByIdOrNull(targetId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        return MemberQueryModelDto.from(member)
    }
}