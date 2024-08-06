package kr.dohoonkim.blog.restapi.units.interfaces.member.dto

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberJoinRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class MemberJoinRequestTest: DtoValidation() {

    val nicknameLength = "닉네임은 최소 4글자, 최대 32글자입니다."
    val nicknameBlank = "닉네임이 입력되어야 합니다."
    val emailFormat = "잘못된 이메일 형식입니다."
    val passwordBlank = "패스워드에는 공백문자가 들어올 수 없습니다."
    val passwordLength = "패스워드는 최소 8글자 최대 255자 입니다."

    init {

        Given("모든 폼이 정상으로 제출된다") {
            val request = MemberJoinRequest(
                nickname = "nickname",
                email = "email@dohoon-kim.kr",
                password = "password1234"
            )

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.nickname shouldBe "nickname"
                    request.email shouldBe "email@dohoon-kim.kr"
                    request.password shouldBe "password1234"
                }
            }
        }

        Given("닉네임에 공백이 입력된다") {
            val request = MemberJoinRequest(
                nickname = " ",
                email = "email@dohoon-kim.kr",
                password = "password1234"
            )

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == nicknameBlank } shouldNotBe null
                }
            }
        }

        Given("닉네임 길이 제한을 위반하는 입력이 주어진다") {
            val request = MemberJoinRequest(
                nickname = "na",
                email = "email@dohoon-kim.kr",
                password = "password1234"
            )

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == nicknameLength } shouldNotBe null
                }
            }
        }

        Given("잘못된 이메일 형식이 주어진다") {
            val request = MemberJoinRequest(
                nickname = "nickname",
                email = "email@dohoon-kim@kr",
                password = "password1234"
            )

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == emailFormat } shouldNotBe null
                }
            }
        }

        Given("패스워드가 공백으로 주어진다") {
            val request = MemberJoinRequest(
                nickname = "nickname",
                email = "email@dohoon-kim.kr",
                password = "        "
            )

            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == passwordBlank } shouldNotBe null
                }
            }
        }

        Given("잘못된 길이의 패스워드가 주어진다") {
            val request = MemberJoinRequest(
                nickname = "nickname",
                email = "email@dohoon-kim.kr",
                password = "pas"
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