package kr.dohoonkim.blog.restapi.units.domain.board.repository

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.config.QueryDslConfig
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.domain.board.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.board.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(value=[QueryDslConfig::class])
class CategoryRepositoryTest(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository
): BehaviorSpec() {

    private lateinit var existName: String
    private lateinit var categories: List<Category>
    private var existId: Long = 0L

    init {
        extension(SpringExtension)

        beforeContainer {
            categoryRepository.deleteAll()
            categories = createCategory(5)
            categories = categoryRepository.saveAll(categories)
            existId = categories[0].id
            existName = categories[0].name
        }

        Given("존재하는 ID가 주어지고") {
            When("존재 여부를 조회하면") {
                Then("true가 반환된다") {
                    categoryRepository.existsByCategoryId(existId) shouldBe true
                }
            }
        }

        Given("존재하지 않는 ID가 주어지고") {
            val notExist = 300L
            When("존재 여부를 조회하면") {
                Then("false가 반환된다") {
                    categoryRepository.existsByCategoryId(notExist) shouldBe false
                }
            }
        }

        Given("존재하는 이름이 주어지고") {
            When("존재 여부를 조회하면") {
                Then("true가 반환된다") {
                    categoryRepository.existsByName(existName) shouldBe true
                }
            }

            When("엔티티를 조회하면") {
                val entity = categoryRepository.findByName(existName)
                Then("엔티티가 반환된다") {
                    entity shouldNotBe null
                    entity?.name shouldBe existName
                }
            }
        }

        Given("존재하지 않는 이름이 주어지고") {
            val notExist = "notExist"
            When("존재 여부를 조회하면") {
                Then("false가 반환된다") {
                    categoryRepository.existsByName(notExist) shouldBe false
                }
            }

            When("엔티티를 조회하면") {
                val entity = categoryRepository.findByName(notExist)
                Then("null이 반환된다") {
                    entity shouldBe null
                }
            }
        }

        Given("게시글이 없는 카테고리들이 주어진다") {
            When("카테고리 목록을 조회하면") {
                val categoryLists = categoryRepository.findAllCategory()

                Then("게시글 수가 0으로 조회된다") {
                    categoryLists.forEach {
                        it.count shouldBe 0
                    }
                }
            }
        }

        Given("게시글이 존재하는 카테고리가 주어지고") {
            When("카테고리 목록을 조회하면") {
                Then("해당 카테고리는 게시글 수가 1 이상이어야 한다") {
                    val member = memberRepository.save(createMember(Role.ADMIN))
                    val article = createArticle(member, categories[0])
                    categories[0].addArticle(article)
                    articleRepository.save(article)
                    val categoryList = categoryRepository.findAllCategory()
                    categoryList.filter { it.count > 0 }.size shouldBeGreaterThan 0
                }
            }
        }

        afterContainer {
            categoryRepository.deleteAll()
        }
    }
}