package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember

internal class ArticleSummaryDtoTest: DtoValidation() {

    init {
        Given("Article이 주어진다") {
            val member = createMember(1).first()
            val category = createCategory(1).first()
            val article = createArticle(member, category)
            When("Dto를 생성하면") {
                val dto = ArticleSummaryDto.fromEntity(article)
                Then("생성된다") {
                    dto.id shouldBe article.id
                    dto.title shouldBe article.title
                    dto.createdAt shouldBe article.createdAt
                    dto.author.id shouldBe article.author.id
                    dto.category.id shouldBe article.category.id
                    dto.commentCount shouldBe 0L
                    dto.viewCount shouldBe article.viewCount
                }
            }
        }
    }
}