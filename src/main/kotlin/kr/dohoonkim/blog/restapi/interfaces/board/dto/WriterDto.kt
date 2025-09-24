package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.common.response.BaseResponse
import java.util.UUID

@Schema(description = "작성자 정보")
data class WriterDto(
    @Schema(description = "작성자 ID", example = "user123")
    val id: UUID,
    @Schema(description = "작성자 닉네임", example = "김도훈")
    val nickname: String
) : BaseResponse() {

    companion object {
        fun of(id: UUID, nickname: String) = WriterDto(
            id = id,
            nickname = nickname
        )

        fun from(dto: MemberSummaryDto): WriterDto {
            return WriterDto(
                id = dto.id,
                nickname = dto.nickname
            )
        }
    }
}