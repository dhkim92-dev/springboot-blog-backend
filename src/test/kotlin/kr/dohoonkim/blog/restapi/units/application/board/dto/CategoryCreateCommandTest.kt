package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class CategoryCreateCommandTest: DtoValidation() {

    init {
        Given("생성자 필드가 주어진다") {
            val name = "category-1"
            When("Command를 생성하면") {
                val command = CategoryCreateCommand(name = name)
                Then("생성된다") {
                    command.name shouldBe  name
                }
            }
        }
    }
}