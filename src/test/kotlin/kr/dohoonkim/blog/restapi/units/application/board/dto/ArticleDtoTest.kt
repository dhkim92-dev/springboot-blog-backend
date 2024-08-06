package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember

internal class ArticleDtoTest: DtoValidation() {

    val member = createMember(1).first()
    val category = createCategory(1).first()

    init {

        Given("Article Entity가 주어진다") {
            val article = createArticle(member, category)
            When("fromEntity를 호출하면") {
                val dto = ArticleDto.fromEntity(article)
                Then("dto가 반환된다") {
                    dto.id shouldBe article.id
                    dto.title shouldBe article.title
                    dto.author.id shouldBe article.author.id
                    dto.category.id shouldBe article.category.id
                    dto.contents shouldBe article.contents
                    dto.createdAt shouldBe article.createdAt
                    dto.commentCount shouldBe 0L
                    dto.viewCount shouldBe 0L
                }
            }
        }
    }
}