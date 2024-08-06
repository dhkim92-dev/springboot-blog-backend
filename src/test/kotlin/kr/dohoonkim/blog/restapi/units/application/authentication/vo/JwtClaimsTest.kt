package kr.dohoonkim.blog.restapi.units.application.authentication.vo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.support.entity.createMember
import java.util.*

internal class JwtClaimsTest: BehaviorSpec({

  Given("생성자 필드가 주어진다") {
      val id = UUID.randomUUID()
      val email = "test@test.co.kr"
      val nickname = "nickname"
      val roles = arrayOf(Role.ADMIN.rolename)
      val isActivated = true

      When("생성자를 호출하면") {
          val claims = JwtClaims(id, email, nickname, roles, isActivated)

          Then("객체가 생성된다") {
              claims.id shouldBe id
              claims.email shouldBe email
              claims.nickname shouldBe nickname
              claims.roles shouldBe roles
              claims.isActivated shouldBe true
          }
      }
  }

    Given("CustomUserDetails가 주어진다") {
        val details = CustomUserDetails.from(createMember(1).first())
        When("fromCustomUserDetails 를 호출하면") {
            val claims = JwtClaims.fromCustomUserDetails(details)
            Then("객체가 생성된다") {
                claims.id shouldBe details.memberId
                claims.email shouldBe details.email
                claims.nickname shouldBe details.nickname
                claims.isActivated shouldBe details.isActivated
                claims.roles.contains(details.role.rolename)
            }
        }
    }

    Given("동일한 두 JwtClaims가 주어진다") {
        val member = createMember(1).first()
        val claim1 = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member))
        val claim2 = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member))

        When("동등 비교를 하면") {
            val result = claim1 == claim2
            Then("true가 반환된다") {
                result shouldBe true
            }
        }

        When("hashCode를 비교하면") {
            val result = claim1.hashCode() == claim2.hashCode()
            Then("동일하다") {
                result shouldBe true
            }
        }
    }

    Given("서로 다른 두 JwtClaims가 주어진다") {
        val member = createMember(2)
        val claim1 = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member[0]))
        val claim2 = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member[1]))

        When("동등 비교를 하면") {
            val result = claim1 == claim2
            Then("false가 반환된다") {
                result shouldBe false
            }
        }

        When("hashCode를 비교하면") {
            val result = claim1.hashCode() == claim2.hashCode()
            Then("서로 다르다") {
                result shouldBe false
            }
        }
    }
})