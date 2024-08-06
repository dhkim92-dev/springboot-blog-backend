package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import kr.dohoonkim.blog.restapi.support.entity.createCategory

internal class CategoryDtoTest: DtoValidation() {

    init {
        Given("Category가 주어진다") {
            val category = createCategory()

            When("fromEntity를 호출하면") {
                val dto = CategoryDto.fromEntity(category)
                Then("Dto가 생성된다") {
                    dto.id shouldBe category.id
                    dto.name shouldBe category.name
                    dto.count shouldBe category.articles.size
                }
            }
        }
    }
}