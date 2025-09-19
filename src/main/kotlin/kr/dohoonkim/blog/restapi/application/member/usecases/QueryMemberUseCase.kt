package kr.dohoonkim.blog.restapi.application.member.usecases

import kr.dohoonkim.blog.restapi.application.member.dto.MemberQueryModelDto
import java.util.UUID

interface QueryMemberUseCase {

    fun getMember(memberId: UUID, targetId: UUID): MemberQueryModelDto
}