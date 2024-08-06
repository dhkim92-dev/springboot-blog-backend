package kr.dohoonkim.blog.restapi.units.application.authentication.dto

import io.kotest.core.spec.style.scopes.GivenScope
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueResult
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

class ReissueResultTest: DtoValidation() {

    init {
        Given("생성자 입력 필드가 주어진다") {
            val accessToken = "12345"

            When("생성자를 호출하면") {
                val dto = ReissueResult(accessToken = accessToken)
                Then("객체가 반환된다") {
                    dto.type shouldBe "Bearer"
                    dto.accessToken shouldBe accessToken
                }
            }
        }
    }
}