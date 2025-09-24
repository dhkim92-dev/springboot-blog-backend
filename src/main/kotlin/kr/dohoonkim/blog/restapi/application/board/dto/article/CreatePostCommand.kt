package kr.dohoonkim.blog.restapi.application.board.dto.article

data class CreatePostCommand(
    val categoryId: Long,
    val title: String,
    val content: String
) {
}