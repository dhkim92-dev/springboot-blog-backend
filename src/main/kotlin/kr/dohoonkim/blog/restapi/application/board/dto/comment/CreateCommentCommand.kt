package kr.dohoonkim.blog.restapi.application.board.dto.comment

import java.util.UUID

data class CreateCommentCommand(
    val postId: UUID,
    val parentId: UUID?,
    val content: String
) {

}