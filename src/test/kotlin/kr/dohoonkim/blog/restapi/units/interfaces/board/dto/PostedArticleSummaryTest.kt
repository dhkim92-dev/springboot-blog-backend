package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticleSummary
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember

internal class PostedArticleSummaryTest: BehaviorSpec({

    val member = createMember(1).first()
    val category = createCategory()
    val article = createArticle(member, category)

    Given("ArticleSummaryDto가 주어진다") {
        val articleSummaryDto = ArticleSummaryDto.fromEntity(article)
        When("변환하면") {
            val result = PostedArticleSummary.valueOf(articleSummaryDto)
            Then("성공한다") {
                result.id shouldBe articleSummaryDto.id
                result.author.id shouldBe articleSummaryDto.author.id
                result.title shouldBe articleSummaryDto.title
                result.category.id shouldBe articleSummaryDto.category.id
                result.createdAt shouldBe articleSummaryDto.createdAt
                result.viewCount shouldBe articleSummaryDto.viewCount
                result.commentCount shouldBe articleSummaryDto.commentCount
            }
        }
    }
})