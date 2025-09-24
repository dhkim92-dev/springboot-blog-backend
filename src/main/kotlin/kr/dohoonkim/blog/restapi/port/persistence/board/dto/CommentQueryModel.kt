package kr.dohoonkim.blog.restapi.port.persistence.board.dto

import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import java.time.LocalDateTime
import java.util.UUID

data class CommentQueryModel(
    val id: UUID,
    val parentId: UUID?,
    val postId: UUID,
    val writer: MemberSummaryDto,
    val content: String,
    val createdAt: LocalDateTime,
) {

}