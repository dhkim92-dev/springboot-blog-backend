package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.comment.CreateCommentCommand
import java.util.UUID

@Schema(description = "대댓글 등록 요청")
data class CreateReplyRequest(
    @Schema(description = "댓글 내용", example = "대댓글 내용입니다.")
    val content: String
) {

    fun toCommand(postId: UUID, commentId: UUID) = CreateCommentCommand(
        postId = postId,
        parentId = commentId,
        content = content
    )
}