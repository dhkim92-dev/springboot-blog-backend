package kr.dohoonkim.blog.restapi.units.interfaces.authentication.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

class LoginRequestTest: DtoValidation() {


    init {
        Given("email 형식이 아닌 email이 주어진다") {
            val request = LoginRequest(
                email = " ",
                password = "password1234"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach { it.message shouldBe "이메일 형식이 제출되어야 합니다" }
                }
            }
        }

        Given("password 가 공백으로 주어진다") {
            val request = LoginRequest("email@email.com", " ")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == "패스워드가 제출되어야 합니다" } shouldNotBe null

                }
            }
        }

        Given("길이 제한을 위반한 패스워드가 주어진다") {
            val request =  LoginRequest("email@email.com", "pass")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == "패스워드는 8자 이상 64자 이하여야 합니다" } shouldNotBe null
                }
            }
        }

        Given("폼이 정상적으로 제출된다") {
            val request = LoginRequest("email@email.com", password="password1234")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                }
            }
        }
    }
}