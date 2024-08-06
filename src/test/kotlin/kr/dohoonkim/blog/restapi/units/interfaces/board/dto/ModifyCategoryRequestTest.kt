package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyCategoryRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class ModifyCategoryRequestTest: DtoValidation() {

    private val nameBlank = "카테고리 이름이 입력되어야 합니다"
    private val nameLength = "카테고리 이름은 1자 이상 16자 이하입니다"

    init {
        Given("카테고리 명이 공백으로 주어진다") {
            val request = ModifyCategoryRequest(
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
            val request = ModifyCategoryRequest(
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

        Given("정상적인 폼이 제출된다") {
            val request = ModifyCategoryRequest(name = "category")

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.name shouldBe "category"
                }
            }
        }
    }
}