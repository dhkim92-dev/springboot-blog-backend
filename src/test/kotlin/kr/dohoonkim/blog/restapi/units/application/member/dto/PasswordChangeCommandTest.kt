package kr.dohoonkim.blog.restapi.units.application.member.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.member.dto.PasswordChangeCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import java.util.*

class PasswordChangeCommandTest: DtoValidation() {
    init {
        val validPassword = "123456789"
        val memberId = UUID.randomUUID()
        val blankError = "invalid password"
        val lengthError = "password required at least 8 at most 64 characters"

        Given("정상적인 커맨드가 주어진다") {
            val command = PasswordChangeCommand(memberId, validPassword, validPassword)
            When("검증하면") {
                val result = validator.validate(command)
                Then("통과한다") {
                    result.isEmpty() shouldBe true
                    command.memberId shouldBe memberId
                    command.currentPassword shouldBe validPassword
                    command.newPassword shouldBe validPassword
                }
            }
        }

        Given("변경 비밀번호가 공백으로 주어진다") {
            val command = PasswordChangeCommand(memberId, validPassword, "                 ")
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach { it.message shouldBe blankError }
                }
            }
        }

        Given("길이 제한을 지키지 않은 변경 패스워드가 주어진다") {
            val invalidPassword = UUID.randomUUID().toString() + UUID.randomUUID().toString()
            val command = PasswordChangeCommand(memberId, validPassword, invalidPassword)
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach { it.message shouldBe lengthError }
                }
            }
        }
    }
}