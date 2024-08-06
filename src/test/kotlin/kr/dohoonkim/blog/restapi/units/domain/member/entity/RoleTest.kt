package kr.dohoonkim.blog.restapi.units.domain.member.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.domain.member.Role

internal class RoleTest: BehaviorSpec({

    Given("rolename으로 Role을 생성한다") {
        When("ROLE_ADMIN이 주어지면") {
            val rolename = "ROLE_ADMIN"
            Then("ADMIN이 반환된다") {
                val role = Role.from(rolename)

                role shouldBe Role.ADMIN
            }
        }

        When("ROLE_MEMBER가 주어지면") {
            val rolename = "ROLE_MEMBER"
            Then("GUEST가 반환된다") {
                val role = Role.from(rolename)
                role shouldBe Role.MEMBER
            }
        }

        When("그외의 문자열이 주어지면") {
            Then("GUEST가 반환된다") {
                val role = Role.from("a")
                role shouldBe Role.GUEST
            }
        }
    }
})