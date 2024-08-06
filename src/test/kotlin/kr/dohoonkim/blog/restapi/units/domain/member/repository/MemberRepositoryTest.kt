package kr.dohoonkim.blog.restapi.units.domain.member.repository

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.config.QueryDslConfig
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.util.UUID


@DataJpaTest
@Import(value=[QueryDslConfig::class])
class MemberRepositoryTest @Autowired constructor(
    private val memberRepository: MemberRepository
) : BehaviorSpec() {

    private lateinit var existEmail: String
    private lateinit var existsNickname: String
    private lateinit var existsMemberId: UUID

    init {
        extension(SpringExtension)

        beforeContainer {
            memberRepository.deleteAll()
            val members = createMember(20)
            existEmail = members[0].email
            existsNickname = members[0].nickname
            existsMemberId = members[0].id
            memberRepository.saveAll(members)
        }

        Given("새로운 회원 엔티티가 주어진다") {
            val member = createMember()

            When("save를 호출하면") {
                val savedMember = memberRepository.save(member)

                Then("저장된다") {
                    savedMember.id shouldNotBe 0L
                    savedMember.role shouldBe member.role
                    savedMember.nickname shouldBe member.nickname
                    savedMember.email shouldBe member.email
                    savedMember.password shouldBe member.password
                }
            }
        }

        Given("저장된 엔티티가 주어진다") {
            val savedEntity = memberRepository.save(createMember())
            val original = savedEntity.nickname
            When("필드를 수정하고 저장하면") {
                savedEntity.updateNickname("modified")
                val modifiedEntity = memberRepository.save(savedEntity)
                Then("저장된다") {
                    modifiedEntity.id shouldBe savedEntity.id
                    modifiedEntity.nickname shouldBe savedEntity.nickname
                }
            }
        }

        Given("회원 가입이 된 이메일 주소가 주어진다") {
//            val member = createMember(1)[0]
//            member.updateEmail(existEmail)
//            memberRepository.save(member)
            When("이메일로 회원 정보를 조회하면") {
                val entity = memberRepository.findByEmail(existEmail)
                Then("엔티티가 조회된다") {
                    entity shouldNotBe null
                    entity?.email shouldBe existEmail
                }
            }

            When("이메일로 회원 존재 여부를 조회하면") {
                Then("true가 반환된다") {
                    memberRepository.existsByEmail(existEmail) shouldBe true
                }
            }
        }

        Given("회원 가입이 되지 않은 이메일이 주어진다") {
            val email = "not-exist@email.co.kr"

            When("이메일로 회원 정보를 조회하면") {
                val entity = memberRepository.findByEmail(email)
                Then("null이 반환된다") {
                    entity shouldBe null
                }
            }

            When("이메일로 회원이 존재하는지 확인하면") {
                Then("false가 반환된다") {
                    memberRepository.existsByEmail(email) shouldBe false
                }
            }
        }

        Given("사용중인 닉네임이 주어진다") {
            When("사용자 존재 여부를 조회하면") {
                Then("true가 반환된다 ") {
                    memberRepository.existsByNickname(existsNickname) shouldBe true
                }
            }
        }

        Given("사용중이지 않은 닉네임이 주어진다") {
            val nickname = "notExistsNickname"
            When("사용자 존재 여부를 조회하면") {
                Then("false가 반환된다") {
                    memberRepository.existsByNickname(nickname) shouldBe false
                }
            }
        }

        Given("존재하는 Member Id가 주어진다") {
            When("memberId로 조회하면") {
                Then("멤버 엔티티가 반환된다") {
                    shouldNotThrowAny {
                        val member = memberRepository.findByMemberId(existsMemberId)
                        member.id shouldBe existsMemberId
                    }
                }
            }
        }

        Given("존재하지 않는 MemberId가 주어진다") {
            val notExistMemberId = UUID.randomUUID()
            When("memberId로 조회하면") {
                Then("NotFoundException이 발생한다") {
                    shouldThrow<NotFoundException> {
                        memberRepository.findByMemberId(notExistMemberId)
                    }
                }
            }
        }


        afterEach {
            clearAllMocks()
        }
    }
}