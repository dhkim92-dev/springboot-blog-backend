package kr.dohoonkim.blog.restapi.application.board.dto.article

import java.util.UUID

class UpdatePostCommand(
    val postId: UUID,
    val categoryId: Long?,
    val title: String?,
    val content: String?
) {
}