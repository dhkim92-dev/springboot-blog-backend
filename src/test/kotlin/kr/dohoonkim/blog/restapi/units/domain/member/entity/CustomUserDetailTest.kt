package kr.dohoonkim.blog.restapi.units.domain.member.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.support.createMember

internal class CustomUserDetailTest: BehaviorSpec({

    Given("Member가 주어지고") {
        val member = createMember(1)[0]

        When("CustomUserDetail을 생성하면") {
            val customUserDetail = CustomUserDetails.from(member)

            Then("정상적으로 생성된다") {
                customUserDetail.email shouldBe member.email
                customUserDetail.nickname shouldBe member.nickname
                customUserDetail.role shouldBe member.role
                customUserDetail.memberId shouldBe member.id
                customUserDetail.isActivated shouldBe member.isActivated
            }
        }
    }

    Given("CustomuserDetail이 주어지고") {
        val member = createMember(1)[0]
        val customUserDetail = CustomUserDetails.from(member)
        When("getAuthorities() 를 호출하면") {
            Then("권한 목록이 조회된다") {
                customUserDetail.authorities.forEach {
                    it.toString() shouldBe member.role.rolename
                }
            }
        }

        When("getUsename() 을 호출하면") {
            Then("email이 반환된다") {
                customUserDetail.username shouldBe member.email
            }
        }

        When("getPassword() 를 호출하면") {
            Then("정상적으로 조회된다") {
                customUserDetail.password shouldBe member.password
            }
        }

        When("isAccountNonExpired() 가 호출되면") {
            Then("true가 반환된다") {
                customUserDetail.isAccountNonExpired shouldBe true
            }
        }

        When("isAccountNonLocked() 가 호출되면") {
            Then("true가 반환된다") {
                customUserDetail.isAccountNonLocked shouldBe true
            }
        }

        When("isCredentailsNonExpired()가 호출되면") {
            Then("true가 반환된다") {
                customUserDetail.isCredentialsNonExpired shouldBe true
            }
        }

        When("isEnabled() 가 호출되면") {
            Then("isActivated 가 반환된다") {
                customUserDetail.isEnabled shouldBe member.isActivated
            }
        }
    }
})