package kr.dohoonkim.blog.restapi.application.board.dto.comment

import java.util.UUID

data class UpdateCommentCommand(
    val commentId: UUID,
    val content: String
) {
}