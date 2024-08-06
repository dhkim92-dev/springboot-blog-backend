package kr.dohoonkim.blog.restapi.interfaces.member.dto

import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import java.util.*

data class MemberSummaryVo(val id: UUID, val nickname: String) {
    companion object {
        fun valueOf(memberSummaryDto: MemberSummaryDto): MemberSummaryVo {
            return MemberSummaryVo(id = memberSummaryDto.id, nickname = memberSummaryDto.nickname)
        }
    }
}
