package kr.dohoonkim.blog.restapi.units.interfaces.member.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberSummaryVo
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import java.util.*

class MemberSummaryVoTest: DtoValidation() {

    init {
        Given("생성자 필드가 모두 주어진다") {
            val id = UUID.randomUUID()
            val nickname = "nickname"

            When("생성자를 호출하면") {
                val vo = MemberSummaryVo(id, nickname)
                Then("생성된다") {
                    vo.id shouldBe id
                    vo.nickname shouldBe nickname
                }
            }
        }

        Given("MemberSummaryDto가 주어진다") {
            val dto = MemberSummaryDto(
                id = UUID.randomUUID(),
                nickname = "nickname"
            )
            When("valueOf를 호출하면") {
                val vo = MemberSummaryVo.valueOf(dto)
                Then("생성된다") {
                    vo.id shouldBe dto.id
                    vo.nickname shouldBe dto.nickname
                }
            }
        }
    }
}