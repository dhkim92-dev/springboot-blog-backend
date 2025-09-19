package kr.dohoonkim.blog.restapi.application.member.usecases

import kr.dohoonkim.blog.restapi.application.member.dto.CreateOAuth2MemberCommand
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto

interface CreateOAuth2MemberUseCase {

    fun create(command: CreateOAuth2MemberCommand): MemberDto
}