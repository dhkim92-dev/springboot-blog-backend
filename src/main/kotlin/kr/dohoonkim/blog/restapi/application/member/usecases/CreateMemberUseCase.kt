package kr.dohoonkim.blog.restapi.application.member.usecases

import kr.dohoonkim.blog.restapi.application.member.dto.CreateMemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto

interface CreateMemberUseCase {

    fun createMember(command: CreateMemberCommand): MemberDto
}