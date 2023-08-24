package kr.dohoonkim.blog.restapi.domain.article.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.domain.article.QCategory.Companion.category
import kr.dohoonkim.blog.restapi.domain.article.QArticle.Companion.article

class CategoryRepositoryCustomImpl(private val queryFactory : JPAQueryFactory) : CategoryRepositoryCustom {

    override fun existsByName(name: String): Boolean {
        val result = queryFactory
            .selectOne()
            .from(category)
            .where(eqName(name))
            .fetchOne()

        return result != null
    }

    override fun existsByCategoryId(id: Long): Boolean {
        val result = queryFactory
            .selectOne()
            .from(category)
            .where(eqId(id))
            .fetchOne()

        return result != null
    }

    override fun findAllCategory(): List<CategoryDto> {
        val ret = queryFactory
            .select(Projections.constructor(
                CategoryDto::class.java,
                category.id,
                category.name,
                article.count()
            ))
            .from(category)
            .leftJoin(article)
            .on(category.id.eq(article.category.id))
            .groupBy(category.id)
            .fetch()

        ret.sortBy { t -> t.count }
        ret.reverse()

        return ret
    }

    private fun eqName(name : String) : BooleanExpression {
        return category.name.eq(name)
    }

    private fun eqId(id : Long) : BooleanExpression {
        return category.id.eq(id)
    }

}