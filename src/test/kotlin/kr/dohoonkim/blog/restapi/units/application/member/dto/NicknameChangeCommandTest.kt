package kr.dohoonkim.blog.restapi.units.application.member.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.member.dto.NicknameChangeCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import java.util.*

class NicknameChangeCommandTest: DtoValidation() {

    init {
        val validNickname = "validNickname"
        val memberId = UUID.randomUUID()
        val blankErrorMessage = "nickname can not be null"

        Given("정상적인 닉네임이 주어진다") {
            val command = NicknameChangeCommand(memberId= memberId, nickname = validNickname)
            When("검증하면") {
                val result = validator.validate(command)
                Then("통과한다") {
                    result.isEmpty() shouldBe true
                    command.nickname shouldBe validNickname
                    command.memberId shouldBe memberId
                }
            }
        }

        Given("빈 문자열이 주어진다") {
            val command = NicknameChangeCommand(memberId, " ")
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach {
                        it.message shouldBe blankErrorMessage
                    }
                }
            }
        }
    }
}