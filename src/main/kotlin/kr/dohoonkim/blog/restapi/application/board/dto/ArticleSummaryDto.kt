package kr.dohoonkim.blog.restapi.application.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ArticleSummaryDto(
    val id : UUID,
    val author : MemberSummaryDto,
    val category : CategorySummaryDto,
    val title : String,
    val createdAt : LocalDateTime,
    val viewCount : Long,
    val commentCount : Long
) {
    companion object {
        fun fromEntity(article : Article) : ArticleSummaryDto {
            return ArticleSummaryDto(
                id=article.id,
                author = MemberSummaryDto.fromEntity(article.author),
                title = article.title,
                category = CategorySummaryDto(article.category.id, article.category.name),
                createdAt = article.createdAt,
                viewCount = article.viewCount,
                commentCount = 0L // TODO("comment 구현 후 재개") article.comments.size.toLong()
            )
        }
    }
}
