package kr.dohoonkim.blog.restapi.port.persistence.board

import kr.dohoonkim.blog.restapi.port.persistence.board.dto.ArticleQueryModel
import java.util.UUID

interface ArticleRepositoryCustom {

    fun getArticleById(id: UUID): ArticleQueryModel?

    fun getArticlesByCategoryIdWithPagination(
        categoryId: Long?,
        cursor: UUID?,
        size: Int
    ): List<ArticleQueryModel>
}