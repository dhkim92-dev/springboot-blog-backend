package kr.dohoonkim.blog.restapi.domain.article.repository

import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import java.time.LocalDateTime
import java.util.*

interface ArticleRepositoryCustom {

    fun findByArticleId(articleId: UUID): ArticleDto?

    fun findArticles(categoryName: String?, createdAt: LocalDateTime?, cursorDirection: String?, pageSize: Long) : List<ArticleSummaryDto>

}