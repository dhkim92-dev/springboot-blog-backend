package kr.dohoonkim.blog.restapi.port.persistence.board

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.domain.board.QArticle
import kr.dohoonkim.blog.restapi.domain.board.QCategory
import kr.dohoonkim.blog.restapi.domain.member.QMember
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.port.persistence.board.dto.ArticleQueryModel
import java.util.UUID

class ArticleRepositoryCustomImpl(
    private val qf: JPAQueryFactory
): ArticleRepositoryCustom {

    override fun getArticleById(id: UUID): ArticleQueryModel? {
        val article = QArticle.article
        val member = QMember.member
        val category = QCategory.category

        return defaultSelect(article, member, category)
            .where(article.id.eq(id)
                .and(article.isDeleted.isFalse)
            )
            .fetchOne()
    }

    override fun getArticlesByCategoryIdWithPagination(
        categoryId: Long?,
        cursor: UUID?,
        size: Int
    ): List<ArticleQueryModel> {
        val article = QArticle.article
        val member = QMember.member
        val category = QCategory.category

        return defaultSelect(article, member, category)
            .where(
                loeCursorId(article, cursor),
                withCategory(article, categoryId),
                article.isDeleted.isFalse
            )
            .orderBy(article.id.desc())
            .limit(size.toLong())
            .fetch()
    }

    private fun loeCursorId(article: QArticle, cursor: UUID?): BooleanExpression? {
        return if ( cursor == null ) null
        else article.id.loe(cursor)
    }

    private fun withCategory(article: QArticle, categoryId: Long?): BooleanExpression? {
        return if ( categoryId == null ) null
        else article.category.id.eq(categoryId)
    }

    private fun defaultSelect(
        article: QArticle = QArticle.article,
        member: QMember = QMember.member,
        category: QCategory = QCategory.category
    ) = qf.select(
        Projections.constructor(ArticleQueryModel::class.java,
            article.id,
            projectMember(member),
            projectCategory(category),
            article.title,
            article.contents,
            article.createdAt,
            article.updatedAt,
            Expressions.constant<Long>(0L),
            Expressions.constant<Long>(0L),
            Expressions.constant<Long>(0L)
        )
    ).from(article)
        .leftJoin(member)
        .on(article.author.id.eq(member.id))
        .leftJoin(category)
        .on(article.category.id.eq(category.id))

    private fun projectMember(member: QMember) = Projections.constructor(
        MemberSummaryDto::class.java,
        member.id,
        member.nickname
    )

    private fun projectCategory(category: QCategory) = Projections.constructor(
        CategoryDto::class.java,
        category.id,
        category.name,
        Expressions.constant(0L)
    )
}