package kr.dohoonkim.blog.restapi.units.interfaces.member.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.member.dto.NicknameChangeRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class NicknameChangeRequest: DtoValidation() {

    val nicknameBlank = "닉네임이 입력되어야 합니다."
    val nicknameLength = "닉네임은 최소 4글자 최대 32글자 입니다."

    init {

        Given("정상 입력이 주어진다") {
            val request = NicknameChangeRequest(nickname = "nickname")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.nickname shouldBe "nickname"
                }
            }
        }

        Given("공백문자가 주어진다") {
            val request = NicknameChangeRequest(nickname = " ")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find{it.message == nicknameBlank} shouldNotBe null
                }
            }
        }

        Given("길이 제한을 위반하는 입력이 주어진다") {
            val request = NicknameChangeRequest("ni")
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find{it.message == nicknameLength} shouldNotBe null
                }
            }
        }
    }
}