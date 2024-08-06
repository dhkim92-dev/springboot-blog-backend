package kr.dohoonkim.blog.restapi.units.application.member.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.member.dto.EmailChangeCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import java.util.*

class EmailChangeCommandTest: DtoValidation() {

    init {
        val memberId = UUID.randomUUID()
        val email = "admin@dohoon-kim.kr"
        Given("사용자 아이디와 올바른 Email이 주어진다") {
            val command = EmailChangeCommand(
                memberId = memberId,
                email = email
            )

            When("DTO를 검증하면") {
                val result = validator.validate(command)

                Then("통과된다") {
                    result.isEmpty() shouldBe true
                    command.memberId shouldBe  memberId
                    command.email shouldBe email
                }
            }
        }

        Given("사용자 아이디와 잘못된 Email이 주어진다") {
            val command = EmailChangeCommand(
                memberId = memberId,
                email = "invalidate@email--sdf111"
            )
            When("DTO를 검증하면") {
                val result = validator.validate(command)

                Then("에러가 발생한다") {
                    result.forEach {
                        it.message shouldBe "email should be email format"
                    }
                }
            }
        }
    }
}