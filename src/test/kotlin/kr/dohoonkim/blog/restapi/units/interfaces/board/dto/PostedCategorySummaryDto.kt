package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedCategorySummary
import kr.dohoonkim.blog.restapi.support.entity.createCategory

internal class PostedCategorySummaryDto: BehaviorSpec({

    val category = createCategory()

    Given("CategorySummaryDto가 주어진다") {
        val categorySummaryDto = CategorySummaryDto(category.id, category.name)

        When("변환을 시도하면") {
            val result = PostedCategorySummary.valueOf(categorySummaryDto)
            Then("성공한다") {
                result.id shouldBe categorySummaryDto.id
                result.name shouldBe categorySummaryDto.name
            }
        }
    }

    Given("CategoryDto가 주어진다") {
        val categoryDto = CategoryDto.fromEntity(category)
        When("변환을 시도하면") {
            val result = PostedCategorySummary.valueOf(categoryDto)
            Then("성공한다") {
                result.id shouldBe categoryDto.id
                result.name shouldBe categoryDto.name
            }
        }
    }
})