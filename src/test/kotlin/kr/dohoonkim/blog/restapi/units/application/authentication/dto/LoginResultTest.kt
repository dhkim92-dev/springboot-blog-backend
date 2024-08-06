package kr.dohoonkim.blog.restapi.units.application.authentication.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

class LoginResultTest: DtoValidation() {

    init {
        Given("생성자 필드가 주어진다") {
            val refreshToken =  "12345"
            val accessToken = "12345"

            When("생성자를 호출하면") {
                val dto = LoginResult(refreshToken=refreshToken, accessToken = accessToken)
                Then("객체가 생성된다") {
                    dto.accessToken shouldBe accessToken
                    dto.refreshToken shouldBe refreshToken
                }
            }
        }
    }
}