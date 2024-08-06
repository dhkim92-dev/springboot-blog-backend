package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostCategoryRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class PostCategoryRequestTest: DtoValidation() {

    private val nameBlank = "카테고리 이름이 입력되어야 합니다"
    private val nameLength = "카테고리 이름은 1자 이상 16자 이하입니다"

    init {

        Given("정상 폼이 주어진다") {
            val request = PostCategoryRequest(name="category")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    request.name shouldBe "category"
                    result.isEmpty() shouldBe true
                }
            }
        }

        Given("카테고리 명이 공백으로 주어진다") {
            val request = PostCategoryRequest(
                name = ""
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == nameBlank } shouldNotBe null
                }
            }
        }

        Given("길이를 만족하지 않는 카테고리 명이 주어진다") {
            val request = PostCategoryRequest(
                name = "11111111111111111111111111111111"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == nameLength} shouldNotBe null
                }
            }
        }
    }
}