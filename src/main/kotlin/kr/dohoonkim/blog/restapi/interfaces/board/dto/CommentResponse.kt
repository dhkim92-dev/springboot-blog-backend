package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.comment.CommentDto
import kr.dohoonkim.blog.restapi.common.response.BaseResponse
import java.util.UUID

@Schema(description = "댓글 응답")
data class CommentResponse(
    val id: UUID,
    val parentId: UUID?,
    val postId: UUID,
    val writer: WriterDto,
    val content: String,
    val createdAt: String,
    val depth: Int
): BaseResponse() {

    companion object {
        fun from(dto: CommentDto): CommentResponse {
            return CommentResponse(
                id = dto.id,
                parentId = dto.parentId,
                postId = dto.postId,
                writer = WriterDto.from(dto.author),
                content = dto.content,
                createdAt = dto.createdAt.toString(),
                depth = if (dto.parentId == null) 0 else 1
            )
        }
    }
}