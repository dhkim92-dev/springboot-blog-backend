package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.*
import java.time.LocalDateTime
import java.util.*

interface ArticleService {

    fun createArticle(memberId : UUID, articleCreateDto: ArticleCreateDto): ArticleDto

    fun modifyArticle(memberId : UUID, articleModifyDto: ArticleModifyDto): ArticleDto

    fun deleteArticle(memberId : UUID, articleId: UUID): Unit

    fun getListOfArticles(categoryId: Long, cursor: LocalDateTime?, pageSize: Long): List<ArticleSummaryDto>

    fun getArticle(articleId: UUID): ArticleDto
}