package kr.dohoonkim.blog.restapi.application.board.dto.article

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.port.persistence.board.dto.ArticleQueryModel
import java.time.LocalDateTime
import java.util.UUID

data class PostSummaryDto(
    val id: UUID,
    val title: String,
    val author: MemberSummaryDto,
    val category: CategoryDto,
    val createdAt: LocalDateTime,
    val viewCount: Long = 0L,
    val commentCount: Long = 0L,
    val likeCount: Long = 0L
) {

    companion object {
        fun from(queryModel: ArticleQueryModel): PostSummaryDto {
            return PostSummaryDto(
                id = queryModel.id,
                title = queryModel.title,
                author = queryModel.author,
                category = queryModel.category,
                createdAt = queryModel.createdAt,
                viewCount = queryModel.viewCount,
                commentCount = queryModel.replyCount,
                likeCount = queryModel.likeCount
            )
        }
    }
}