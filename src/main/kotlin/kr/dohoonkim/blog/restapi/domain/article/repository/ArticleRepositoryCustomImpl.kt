package kr.dohoonkim.blog.restapi.domain.article.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCode.MEMBER_NOT_FOUND
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.domain.article.QArticle.Companion.article
import kr.dohoonkim.blog.restapi.domain.article.QCategory.Companion.category
import kr.dohoonkim.blog.restapi.domain.member.QMember.Companion.member
import java.time.LocalDateTime
import java.util.*

/**
 * ArticleRepositoryCustom 구현체
 * @author dhkim92.dev@gmail.com
 */
class ArticleRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ArticleRepositoryCustom {


    override fun findByArticleId(articleId: UUID): ArticleDto {
        return queryFactory.select(
            Projections.constructor(
                ArticleDto::class.java,
                Expressions.constant(articleId),
                article.title,
                article.contents,
                Projections.constructor(
                    MemberSummaryDto::class.java,
                    member.id,
                    member.nickname
                ),
                Projections.constructor(
                    CategorySummaryDto::class.java,
                    category.id,
                    category.name
                ),
                article.createdAt,
                article.viewCount
            )
        )
            .from(article)
            .leftJoin(member)
            .on(member.id.eq(article.author.id))
            .leftJoin(category)
            .on(article.category.id.eq(category.id))
            .where(article.id.eq(articleId))
            .fetchOne()
            ?: throw EntityNotFoundException(MEMBER_NOT_FOUND)
    }

    override fun findArticles(
        categoryId: Long,
        createdAt: LocalDateTime?,
        pageSize: Long
    ): List<ArticleSummaryDto> {
        val data = queryFactory
            .select(
                Projections.constructor(
                    ArticleSummaryDto::class.java,
                    article.id,
                    Projections.constructor(
                        MemberSummaryDto::class.java,
                        member.id,
                        member.nickname,
                    ),
                    Projections.constructor(
                        CategorySummaryDto::class.java,
                        category.id,
                        category.name
                    ),
                    article.title,
                    article.createdAt,
                    article.viewCount,
                    Expressions.constant(0L)// TODO("comment 구현 후 수정")
                )
            )
            .from(article)
            .leftJoin(category)
            .on(equalCategory())
            .leftJoin(member)
            .on(equalMember())
            .where(
                eqCategoryId(categoryId),
                ltOrEqCreatedAt(createdAt)
            )
            .orderBy(article.createdAt.desc())
            .limit(pageSize + 1)
            .fetch()

        return data
    }

    private fun eqCategoryId(categoryId: Long): BooleanExpression? {
        return if (categoryId == 0L) return null else category.id.eq(categoryId)
    }

    private fun eqCategoryName(categoryName: String?): BooleanExpression? {
        return if (categoryName == null) null else category.name.eq(categoryName)
    }

    private fun ltOrEqCreatedAt(createdAt: LocalDateTime?): BooleanExpression? {
        return if (createdAt == null) null else article.createdAt.loe(createdAt)
    }

    private fun gtCreatedAt(createdAt: LocalDateTime?): BooleanExpression? {
        return if (createdAt == null) null else article.createdAt.gt(createdAt)
    }

    private fun equalCategory(): BooleanExpression {
        return article.category.id.eq(category.id)
    }

    private fun equalMember(): BooleanExpression {
        return article.author.id.eq(member.id)
    }

    private fun orderByCursorDirection(direction: String): OrderSpecifier<LocalDateTime?> {
        return if (direction == "next") article.createdAt.desc()
        else article.createdAt.asc()
    }

    private fun conditionForCursorDirection(createdAt: LocalDateTime?, direction: String?): BooleanExpression? {
        if (createdAt == null) {
            return null
        }

        if (direction == null || direction == "next") {
            return ltOrEqCreatedAt(createdAt)
        }

        return gtCreatedAt(createdAt)
    }
}