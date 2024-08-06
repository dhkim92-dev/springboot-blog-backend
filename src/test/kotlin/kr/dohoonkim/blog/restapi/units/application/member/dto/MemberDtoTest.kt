package kr.dohoonkim.blog.restapi.units.application.member.dto

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import kr.dohoonkim.blog.restapi.support.entity.createMember

class MemberDtoTest: DtoValidation() {

    val member = createMember(1)[0]

    init {
        Given("Member 엔티티가 주어진다") {
            When("fromEntity를 호출하면") {
                val dto = MemberDto.fromEntity(member)
                Then("정상적으로 변환된다") {
                    dto.id shouldBe member.id
                    dto.email shouldBe member.email
                    dto.nickname shouldBe member.nickname
                    dto.role shouldBe member.role.rolename
                    dto.isActivated shouldBe member.isActivated
                }
            }
        }

        Given("MemberDto 가 주어진다") {
            val dto = MemberDto.fromEntity(member)
            When("JSON으로 변환하면") {
                val json = jacksonObjectMapper().writeValueAsString(dto)
                Then("isActivated가 is_activated 로 표기된다") {
                    val parsedJson = com.jayway.jsonpath.JsonPath.parse(json)
                    (parsedJson.read("$.is_activated") as Boolean) shouldBe dto.isActivated
                }
            }
        }
    }
}