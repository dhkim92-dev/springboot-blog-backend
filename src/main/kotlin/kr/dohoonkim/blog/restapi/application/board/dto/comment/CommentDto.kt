package kr.dohoonkim.blog.restapi.application.board.dto.comment

import kr.dohoonkim.blog.restapi.domain.board.Comment
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.port.persistence.board.dto.CommentQueryModel
import java.time.LocalDateTime
import java.util.UUID

data class CommentDto(
    val id: UUID,
    val parentId: UUID?,
    val postId: UUID,
    val author: MemberSummaryDto,
    val content: String,
    val createdAt: LocalDateTime
) {

    companion object {
        fun from(entity: Comment): CommentDto {
            return CommentDto(
                id = entity.id!!,
                parentId = entity.parent?.id,
                postId = entity.article.id!!,
                author = MemberSummaryDto.from(entity.writer),
                content = entity.content,
                createdAt = entity.createdAt
            )
        }

        fun from(qm: CommentQueryModel): CommentDto {
            return CommentDto(
                id = qm.id,
                parentId = qm.parentId,
                postId = qm.postId,
                author = qm.writer,
                content = qm.content,
                createdAt = qm.createdAt
            )
        }
    }
}