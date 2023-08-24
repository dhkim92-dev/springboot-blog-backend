package kr.dohoonkim.blog.restapi.units.repository

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.domain.article.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.domain.article.Category
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.configs.TestJpaConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestJpaConfig::class)
class CategoryRepositoryTest : BehaviorSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var categoryRepository : CategoryRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    init {

        beforeEach {
            member = Member(
                nickname = "tester",
                email = "test@gmail.com",
                isActivated = true,
                password = "1234"
            )

            category1 = Category(name = "test1")
            category2 = Category(name = "test2")
            article1 = createArticle(member, category1)
            article2 = createArticle(member, category1)
        }

        Given("두개의 카테고리가 주어지고") {
            When("카테고리 엔티티의 존재 여부를 판별하려 할 때") {
                Then("아이디로 조회하여, 존재하면 true 가 반환된다.") {
                    categoryRepository.saveAll(listOf(category1, category2))
                    memberRepository.save(member)
                    articleRepository.saveAll(listOf(article1, article2))
                    categoryRepository.existsByCategoryId(category1.id) shouldBe true
                }

                Then("아이디로 조회하여, 존재하지 않으면 false 가 반환된다.") {
                    categoryRepository.saveAll(listOf(category1, category2))
                    memberRepository.save(member)
                    articleRepository.saveAll(listOf(article1, article2))
                    categoryRepository.existsByCategoryId(987) shouldBe false
                }

                Then("카테고리 명으로 조회하여, 존재하면 true 가 반환된다.") {
                    categoryRepository.saveAll(listOf(category1, category2))
                    memberRepository.save(member)
                    articleRepository.saveAll(listOf(article1, article2))
                    categoryRepository.existsByName(category1.name) shouldBe true
                }

                Then("카테고리 명으로 조회하여, 존재하지 않으면 false 가 반환된다.") {
                    categoryRepository.saveAll(listOf(category1, category2))
                    memberRepository.save(member)
                    articleRepository.saveAll(listOf(article1, article2))
                    categoryRepository.existsByName("nothing") shouldBe false
                }
            }

            When("카테고리 리스트를 조회할 때") {
                Then("Category 1의 게시글 수는 2개이고 Category 2의 게시굴 수는 0개이다.") {
                    categoryRepository.saveAll(listOf(category1, category2))
                    memberRepository.save(member)
                    articleRepository.saveAll(listOf(article1, article2))
                    val categories = categoryRepository.findAllCategory()
                    categories.forEach(::println)
                    categories.size shouldBe 2
                    categories[0].count shouldBe 2
                    categories[1].count shouldBe 0
                }
            }
        }

        afterTest {
            println("after category repository size ${categoryRepository.count()}")
        }
    }

    private fun createArticle(member : Member, category : Category) : Article {
        return Article(
            title = "1234",
            contents = "1234",
            author = member,
            category = category
        )
    }

    lateinit var member : Member
    lateinit var article1 : Article
    lateinit var article2 : Article
    lateinit var category1 : Category
    lateinit var category2 : Category
}