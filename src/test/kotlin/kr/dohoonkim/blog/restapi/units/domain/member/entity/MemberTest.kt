package kr.dohoonkim.blog.restapi.units.domain.member.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.support.createMember
import java.util.*

internal class MemberTest: BehaviorSpec({
    var member: Member = Member(UUID.randomUUID())

    beforeEach {
        member = createMember(1)[0]
    }

    Given("nickname 변수를 변경한다") {
        When("문자열이 주어지면") {
            Then("변경된다") {
                member.updateNickname("a")
                member.nickname shouldBe "a"
            }
        }
    }

    Given("비밀번호를 변경한다") {
        When("문자열을 입력하면") {
            val password = "password"
            Then("변경된다") {
                member.updatePassword(password)
                member.password shouldBe password
            }
        }
    }

    Given("email을 변경한다") {
        When("문자열이 주어지면") {
            val email = "new@test.co.kr"
            Then("변경된다") {
                member.updateEmail(email)
                member.email shouldBe email
            }
        }
    }

    Given("Role을 변경한다") {
        When("Role 주어지면") {
            val role = Role.ADMIN
            Then("변경된다") {
                member.role = role
                member.role shouldBe role
            }
        }
    }

    afterEach {
        clearAllMocks()
    }
})