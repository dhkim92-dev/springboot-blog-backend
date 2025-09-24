package kr.dohoonkim.blog.restapi.application.board.dto.article

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.port.persistence.board.dto.ArticleQueryModel
import java.time.LocalDateTime
import java.util.UUID

data class PostDto(
    val id: UUID,
    val title: String,
    val content: String,
    val author: MemberSummaryDto,
    val category: CategoryDto,
    val createdAt: LocalDateTime,
    val viewCount: Long = 0L,
    val replyCount: Long = 0L,
    val likeCount: Long = 0L
) {
    companion object {
        fun from(queryModel: ArticleQueryModel): PostDto {
            return PostDto(
                id = queryModel.id,
                title = queryModel.title,
                content = queryModel.content,
                author = queryModel.author,
                category = queryModel.category,
                createdAt = queryModel.createdAt,
                viewCount = queryModel.viewCount,
                replyCount = queryModel.replyCount,
                likeCount = queryModel.likeCount
            )
        }
    }
}