package kr.dohoonkim.blog.restapi.units.application.member.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import kr.dohoonkim.blog.restapi.support.entity.createMember
import java.util.*

class MemberSummaryDtoTest: DtoValidation() {

    init {

        Given("생성자에 입력값이 주어진다") {
            val id = UUID.randomUUID()
            val nickname = "nickname"
            When("생성자를 호출하면") {
                val dto = MemberSummaryDto(id, nickname)
                Then("생성된다") {
                    dto.id shouldBe id
                    dto.nickname shouldBe nickname
                }
            }
        }

        Given("Member 엔티티가 주어진다") {
            val member = createMember(1).first()
            When("fromEntity를 호출하면") {
                Then("MemberSummaryDto가 반환된다.") {
                    val dto = MemberSummaryDto.fromEntity(member!!)
                    (dto is MemberSummaryDto) shouldBe true
                    dto.id shouldBe member.id
                    dto.nickname shouldBe member.nickname
                }
            }
        }
    }
}