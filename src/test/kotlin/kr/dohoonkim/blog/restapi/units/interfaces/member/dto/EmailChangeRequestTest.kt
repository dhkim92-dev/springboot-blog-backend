package kr.dohoonkim.blog.restapi.units.interfaces.member.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.interfaces.member.dto.EmailChangeRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class EmailChangeRequestTest: DtoValidation() {

    init {

        Given("이메일이 주어진다") {
            val email = "admin@dohoon-kim.kr"
            val dto = EmailChangeRequest(email = email)
            When("검증을 하면") {
                val result = validator.validate(dto)
                Then("통과된다") {
                    result.isEmpty() shouldBe true
                    dto.email shouldBe email
                }
            }
        }

        Given("이메일이 아닌 형식이 주어진다") {
            val email = "nonemail.dohoon-kim.kr"
            val dto = EmailChangeRequest(email)
            When("검증을 하면") {
                val result = validator.validate(dto)
                Then("통과되지 않는다") {
                    result.isEmpty() shouldBe false
                }
            }
        }
    }
}