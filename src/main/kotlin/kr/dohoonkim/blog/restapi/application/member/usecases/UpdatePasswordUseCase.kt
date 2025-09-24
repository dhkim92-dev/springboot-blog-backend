package kr.dohoonkim.blog.restapi.application.member.usecases

import kr.dohoonkim.blog.restapi.application.member.dto.UpdatePasswordCommand
import java.util.UUID

interface UpdatePasswordUseCase {

    /**
     * 멤버의 비밀번호를 업데이트합니다.
     * @param memberId 현재 로그인한 멤버의 ID
     */
    fun updatePassword(memberId: UUID, command: UpdatePasswordCommand)
}