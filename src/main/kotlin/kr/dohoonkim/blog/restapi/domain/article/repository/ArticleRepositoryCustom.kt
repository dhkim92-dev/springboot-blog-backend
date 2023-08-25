package kr.dohoonkim.blog.restapi.domain.article.repository

import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import java.time.LocalDateTime
import java.util.*

interface ArticleRepositoryCustom {

    fun findByArticleId(articleId: UUID): ArticleDto?

    /**
     * 카테고리에 따른 게시글을 조회하여 반환한다.
     * 전체 게시글에 대한 조회는 categoryId 를 0으로 세팅하여 호출한다.
     */
    fun findArticles(categoryId: Long, createdAt: LocalDateTime?, cursorDirection: String?, pageSize: Long) : List<ArticleSummaryDto>

}