package kr.dohoonkim.blog.restapi.application.board.dto

import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import java.time.LocalDateTime
import java.util.UUID

/**
 * 게시물 요약 객체
 * @property id 게시물 ID
 * @property author 작성자 정보 요약
 * @property category 카테고리 정보 요약
 * @property title 게시물 제목
 * @property createdAt 게시물 작성일
 * @property viewCount 조회 수
 * @property commentCount 댓글 수
 */
class ArticleSummaryDto(
    val id: UUID,
    val author: MemberSummaryDto,
    val category: CategorySummaryDto,
    val title: String,
    val createdAt: LocalDateTime,
    val viewCount: Long,
    val commentCount: Long
) {
    companion object {

        /**
         * Article Entity 를 ArticleSummaryDto로 변환한다
         * @param article Article 엔티티
         * @return ArticleSummaryDto 객체
         */
        fun fromEntity(article: Article): ArticleSummaryDto {
            return ArticleSummaryDto(
                id = article.id,
                author = MemberSummaryDto.fromEntity(article.author),
                title = article.title,
                category = CategorySummaryDto(article.category.id, article.category.name),
                createdAt = article.createdAt,
                viewCount = article.viewCount,
                commentCount = 0L
            )
        }
    }
}
