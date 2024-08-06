package kr.dohoonkim.blog.restapi.units.application.member.dto

import io.kotest.matchers.shouldBe
import io.mockk.InternalPlatformDsl.toStr
import kr.dohoonkim.blog.restapi.application.member.dto.MemberCreateCommand
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import java.util.*

class MemberCreateCommandTest: DtoValidation() {
    init {
        val validNickname = "normal"
        val validEmail = "valid@email.com"
        val validPassword="12345678910"
        val passwordLengthErrorMessage = "password required at least 8 at most 64 characters"
        val passwordBlankErrorMessage = "invalid password"
        val nicknameErrorMessage = "invalid nickname"
        val emailErrorMessage = "invalid email"

        Given("정상적인 폼이 입력된 커맨드가 주어진다") {
            val command = MemberCreateCommand(validNickname, validEmail, validPassword)
            When("검증하면") {
                val result = validator.validate(command)
                result.forEach { println(it.message) }
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    command.email shouldBe validEmail
                    command.nickname shouldBe validNickname
                    command.password shouldBe validPassword
                }
            }
        }

        Given("닉네임이 빈 문자열이 입력된 커맨드가 주어진다") {
            val command = MemberCreateCommand("", validEmail, validPassword)
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach{
                        it.message shouldBe nicknameErrorMessage
                    }
                }
            }
        }

        Given("패스워드 길이가 빈 문자열로 주어진다") {
            val command = MemberCreateCommand(validNickname, validEmail, "           ")
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach{
                        it.message shouldBe passwordBlankErrorMessage
                    }
                }
            }
        }

        Given("패스워드 길이가 7자로 주어진다") {
            val password="1234567"
            val command = MemberCreateCommand(validNickname, validEmail, password)
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach{
                        it.message shouldBe passwordLengthErrorMessage
                    }
                }
            }
        }

        Given("패스워드 길이가 65자로 주어진다") {
            val password= UUID.randomUUID().toString()+UUID.randomUUID().toString()
            val command = MemberCreateCommand(validNickname, validEmail, password)
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach{
                        it.message shouldBe passwordLengthErrorMessage
                    }
                }
            }
        }

        Given("이메일 형식이 잘못된 커맨드가 주어진다") {
            val command = MemberCreateCommand(validNickname, "invalidemail.com", validPassword)
            When("검증하면") {
                val result = validator.validate(command)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.forEach {
                        it.message shouldBe emailErrorMessage
                    }
                }
            }
        }
    }
}