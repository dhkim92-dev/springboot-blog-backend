package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import kr.dohoonkim.blog.restapi.support.entity.createCategory

internal class CategorySummaryDtoTest: DtoValidation() {

    init {
        Given("Category 객체가 주어진다") {
            val category = createCategory()
            When("생성자를 호출하면") {
                val dto = CategorySummaryDto(
                    id = category.id,
                    name = category.name
                )
                Then("Dto가 생성된다") {
                    dto.id shouldBe category.id
                    dto.name shouldBe category.name
                }
            }
        }
    }
}