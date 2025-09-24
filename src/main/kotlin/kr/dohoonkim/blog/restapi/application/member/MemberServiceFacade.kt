package kr.dohoonkim.blog.restapi.application.member

import kr.dohoonkim.blog.restapi.application.member.dto.CreateMemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.CreateOAuth2MemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberQueryModelDto
import kr.dohoonkim.blog.restapi.application.member.dto.UpdateMemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.UpdatePasswordCommand
import kr.dohoonkim.blog.restapi.application.member.usecases.CreateMemberUseCase
import kr.dohoonkim.blog.restapi.application.member.usecases.DeleteMemberUseCase
import kr.dohoonkim.blog.restapi.application.member.usecases.QueryMemberUseCase
import kr.dohoonkim.blog.restapi.application.member.usecases.UpdateMemberUseCase
import kr.dohoonkim.blog.restapi.application.member.usecases.UpdatePasswordUseCase
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberServiceFacade(
    private val createMemberUseCase: CreateMemberUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
    private val queryMemberUseCase: QueryMemberUseCase
) {

    fun createMember(command: CreateMemberCommand): MemberDto {
        return createMemberUseCase.createMember(command)
    }

    fun updatePassword(memberId: UUID, command: UpdatePasswordCommand) {
        updatePasswordUseCase.updatePassword(memberId, command)
    }

    fun updateMember(memberId: UUID, command: UpdateMemberCommand) {
        updateMemberUseCase.update(memberId, command)
    }

    fun deleteMember(memberId: UUID, targetId: UUID) {
        deleteMemberUseCase.deleteMember(memberId, targetId)
    }

    fun getMember(memberId: UUID, targetId: UUID): MemberQueryModelDto {
        return queryMemberUseCase.getMember(memberId, targetId)
    }
}