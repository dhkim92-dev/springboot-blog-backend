package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyArticleRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

class ModifyArticleRequestTest: DtoValidation() {

    private val titleLength = "제목은 최소 1글자 최대 255글자 입니다."
    private val categoryLength = "카테고리 명은 최소 1글자 최대 16글자입니다."

    init {
        Given("모든 필드가 주어진다") {
            val request = ModifyArticleRequest(
                title = "title",
                contents = "contents",
                category = "category"
            )

            When("검증하면"){
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.title shouldBe "title"
                    request.contents shouldBe "contents"
                    request.category shouldBe "category"
                }
            }
        }

        Given("전체 필드가 null로 주어진다") {
            val request = ModifyArticleRequest(
                title = null,
                contents = null,
                category = null
            )

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.category shouldBe null
                    request.title shouldBe null
                    request.contents shouldBe null
                }
            }
        }

        Given("길이를 만족하지 않는 제목이 주어진다") {
            val sb = StringBuffer()
            repeat(256) {
                sb.append("a")
            }
            val request = ModifyArticleRequest(
                title = sb.toString(),
                contents = "contents",
                category = "category"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == titleLength } shouldNotBe null
                }
            }
        }

        Given("길이를 만족하지 않는 카테고리 명이 주어진다") {
            val request = ModifyArticleRequest(
                title = "title",
                contents = "contents",
                category = "categoryyyyyyyyyyyyyyyyy"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == categoryLength } shouldNotBe null
                }
            }
        }
    }
}