package kr.dohoonkim.blog.restapi.units.interfaces.member.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.validation.constraints.NotBlank
import kr.dohoonkim.blog.restapi.interfaces.member.dto.PasswordChangeRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import org.hibernate.validator.constraints.Length

internal class PasswordChangeRequestTest: DtoValidation() {

    val currPassBlank ="현재 패스워드가 입력되어야 합니다."
    val newPassBlank  ="변경할 패스워드가 입력되어야 합니다."
    val passwordLength = "패스워드의는 최소 8글자 최대 255글자 입니다."

    init {
        Given("모든 필드가 정상적으로 주어진다.") {
            val request = PasswordChangeRequest("currpass", "newpassword")

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.currentPassword shouldBe "currpass"
                    request.newPassword shouldBe "newpassword"
                }
            }
        }

        Given("현재 비밀번호가 공백으로 입력된다.") {
            val request = PasswordChangeRequest(
                currentPassword = " ",
                newPassword = "newpassword"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find{ it.message==currPassBlank } shouldNotBe null
                }
            }
        }

        Given("바꾸려는 비밀번호가 공백으로 주어진다") {
            val request = PasswordChangeRequest(
                currentPassword = "currentpassword",
                newPassword = " "
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == newPassBlank } shouldNotBe null
                }
            }
        }

        Given("길이 조건을 만족하지 않는 새 비밀번호가 주어진다") {
            val request = PasswordChangeRequest(
                currentPassword = "currentpassword",
                newPassword = "abc"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == passwordLength } shouldNotBe null
                }
            }
        }
    }
}