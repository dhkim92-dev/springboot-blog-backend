package kr.dohoonkim.blog.restapi.units.interfaces.authentication.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.ReissueTokenRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

class ReissueTokenRequestTest: DtoValidation() {

    init {
        Given("Refresh Token이 공백으로 주어진다") {
            val request = ReissueTokenRequest(refreshToken = "")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find{ it.message == "Refresh token이 필요합니다" } shouldNotBe  null
                }
            }
        }
    }
}