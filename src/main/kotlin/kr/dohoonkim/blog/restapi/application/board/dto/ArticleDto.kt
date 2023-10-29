package kr.dohoonkim.blog.restapi.application.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.domain.article.Category
import kr.dohoonkim.blog.restapi.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ArticleDto(
    val id: UUID,
    val title: String,
    val contents: String,
    val author: MemberSummaryDto,
    val category: CategorySummaryDto,
    val createdAt: LocalDateTime,
    val viewCount: Long,
) {
    companion object {
        fun fromEntity(article: Article): ArticleDto {
            return ArticleDto(
                id = article.id,
                title = article.title,
                contents = article.contents,
                author = MemberSummaryDto.fromEntity(article.author),
                category = CategorySummaryDto(article.category.id!!, article.category.name),
                createdAt = article.createdAt,
                viewCount = article.viewCount
            )
        }
    }
}
