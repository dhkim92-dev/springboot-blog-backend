package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class CategoryModifyCommandTest: DtoValidation() {

    init {
        Given("생성자 필드가 주어진다") {
            val id = 1L
            val name = "category-1"
            When("Command를 생성하면") {
                val command = CategoryModifyCommand(id=id, newName = name)
                Then("생성된다") {
                    command.id shouldBe id
                    command.newName shouldBe name
                }
            }
        }
    }
}