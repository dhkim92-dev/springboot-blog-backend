package kr.dohoonkim.blog.restapi.units.repository

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.domain.article.Category
import kr.dohoonkim.blog.restapi.domain.article.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.configs.TestJpaConfig
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.units.repository.MemberRepositoryTest.Companion.createMember
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestJpaConfig::class)
class ArticleRepositoryTest : BehaviorSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var articleRepository: ArticleRepository
    lateinit var member: Member
    lateinit var category1: Category
    lateinit var category2: Category

    init {
        beforeEach {
            member = createMember()
            category1 = Category(name = "test-category-1")
            category2 = Category(name = "test-category-2")
        }

        Given("게시물 하나가 생성되고") {
            When("그 게시물을 아이디로 조회하였을 때") {
                Then("게시물이 정상 조회되어야 한다.") {
                    memberRepository.save(member)
                    categoryRepository.save(category1)
                    val article = createArticle(member, category1)
                    articleRepository.save(article)

                    val searchedArticle = articleRepository.findByArticleId(article.id)

                    searchedArticle!!.id shouldBe article.id
                    searchedArticle!!.author.id shouldBe member.id
                    searchedArticle!!.title shouldBe article.title
                    searchedArticle!!.contents shouldBe article.contents
                    searchedArticle!!.category.name shouldBe category1.name
                    searchedArticle!!.createdAt shouldNotBe null
                }
            }
        }

        Given("두개 이상의 게시물이 생성되고") {
            When("게시물 목록을 카테고리명과 함께 조회하였을 때") {
                Then("게시물 목록 반환 결과가 pageSize + 1 이하의 값이 반환되어야한다.") {
                    val articles = mutableListOf<Article>()

                    memberRepository.save(member)
                    categoryRepository.saveAll(listOf(category1, category2))

                    for (i in 0..4) {
                        articles.add(createArticle(member, category1))
                        articles.add(createArticle(member, category2))
                        Thread.sleep(50)
                    }

                    articleRepository.saveAll(articles)

                    val category1Articles = articleRepository.findArticles(
                        category1.id,
                        null,
                        4L
                    )

                    category1Articles.size shouldBe 5
                    category1Articles.forEach {
                        it.category.name shouldBe category1.name
                    }
                }
            }
        }

        afterEach {
            clearAllMocks()
        }
    }

}