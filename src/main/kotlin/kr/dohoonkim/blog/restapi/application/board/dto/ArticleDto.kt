package kr.dohoonkim.blog.restapi.application.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.domain.article.Article
import java.time.LocalDateTime
import java.util.UUID

/**
 * 게시물 Dto
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 게시물 ID
 * @property title 게시물 제목
 * @property contents 게시물 본문
 * @property author 작성자 정보 요약
 * @property category 카테고리 요약
 * @property createdAt 게시물 작성일
 * @property viewCount 조회 수
 * @property commentCount 댓글 수
 */
data class ArticleDto(
    val id: UUID,
    val title: String,
    val contents: String,
    val author: MemberSummaryDto,
    val category: CategorySummaryDto,
    val createdAt: LocalDateTime,
    val viewCount: Long,
    val commentCount: Long = 0L
) {
    companion object {

        /**
         * Article Entity를 Article Dto로 변환한다
         * @param article Article Entity
         * @return ArticleDto
         */
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
