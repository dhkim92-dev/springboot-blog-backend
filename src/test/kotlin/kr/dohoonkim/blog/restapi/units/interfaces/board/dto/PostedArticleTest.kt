package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticle
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember

internal class PostedArticleTest: BehaviorSpec({

    val member = createMember(1).first()
    val category = createCategory()
    val article = createArticle(member, category)

    Given("ArticleDto가 주어진다") {
        val articleDto = ArticleDto.fromEntity(article)
        When("변환을 하면") {
            val result = PostedArticle.valueOf(articleDto)
            Then("성공한다") {
                result.id shouldBe articleDto.id
                result.author.id shouldBe articleDto.author.id
                result.category.id shouldBe articleDto.category.id
                result.commentCount shouldBe articleDto.commentCount
                result.viewCount shouldBe articleDto.viewCount
                result.title shouldBe articleDto.title
                result.createdAt shouldBe articleDto.createdAt
                result.contents shouldBe articleDto.contents
            }
        }
    }
})