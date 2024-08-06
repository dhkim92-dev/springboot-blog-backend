package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleCreateCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

class ArticleCreateCommandTest: DtoValidation() {

    init {
        Given("생성자 필드값이 주어진다") {
            val title = "title-1"
            val contents = "contents-1"
            val category = "category-1"
            When("Command를 생성하면") {
                val command = ArticleCreateCommand(title, contents, category)
                Then("생성된다") {
                    command.title shouldBe title
                    command.contents shouldBe contents
                    command.category shouldBe category
                }
            }
        }
    }
}