package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedCategory
import kr.dohoonkim.blog.restapi.support.entity.createCategory

internal class PostedCategoryTest: BehaviorSpec({

    val category = createCategory()

    Given("CategoryDto가 주어진다") {
        val categoryDto = CategoryDto.fromEntity(category)
        When("변환을 시도하면") {
            val result = PostedCategory.valueOf(categoryDto)
            Then("성공한다") {
                result.id shouldBe categoryDto.id
                result.name shouldBe categoryDto.name
                result.count shouldBe categoryDto.count
            }
        }
    }
})