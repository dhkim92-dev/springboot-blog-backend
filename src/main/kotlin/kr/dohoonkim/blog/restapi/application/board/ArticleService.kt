package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.*
import java.time.LocalDateTime
import java.util.*

interface ArticleService {

    fun createArticle(articleCreateDto: ArticleCreateDto): ArticleDto

    fun modifyArticle(articleModifyDto: ArticleModifyDto): ArticleDto

    fun deleteArticle(articleId : UUID) : Unit

    fun getListOfArticles(categoryId: Long, cursor: LocalDateTime?, direction: String?, pageSize: Long) : List<ArticleSummaryDto>

    fun getArticle(articleId : UUID) : ArticleDto
}