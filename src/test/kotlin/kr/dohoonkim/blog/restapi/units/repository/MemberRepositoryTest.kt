package kr.dohoonkim.blog.restapi.units.repository

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.configs.TestJpaConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestJpaConfig::class)
class MemberRepositoryTest : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        println("MemberRepositoryTest")
        describe("MemberRepository") {
            context("멤버를 생성하고난 뒤") {
                val member = createMember()
                memberRepository.save(member);

                it("전체 유저 수가 1이어야한다.") {
                    val count = memberRepository.count()
                    count shouldBe 1L
                }
            }
        }

        afterTest {
            memberRepository.deleteAll()
        }
    }

    companion object {
        fun createMember(): Member {
            return Member(
                nickname = "tester",
                email = "tester@gmai.com",
                password = "password",
                isActivated = true
            )
        }
    }

}



