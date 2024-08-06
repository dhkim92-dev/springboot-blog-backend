package kr.dohoonkim.blog.restapi.units.application.board.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleModifyCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class ArticleModifyCommandTest: DtoValidation() {

    init {
        Given("생성자 필드가 주어진다") {
            val title = "title-1"
            val contents = "contents-1"
            val category = null

            When("command를 생성하면") {
                val command = ArticleModifyCommand(
                    title = title,
                    contents = contents,
                    category = category
                )
                Then("생성된다") {
                    command.title shouldBe title
                    command.contents shouldBe contents
                    command.category shouldBe category
                }
            }
        }
    }
}