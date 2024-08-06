package kr.dohoonkim.blog.restapi.units.domain.board.repository

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.config.QueryDslConfig
import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.domain.board.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.board.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createArticles
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.util.UUID

@DataJpaTest
@Import(value=[QueryDslConfig::class])
class ArticleRepositoryTest(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository
): BehaviorSpec() {

    private lateinit var member: Member
    private lateinit var categories: List<Category>
    private lateinit var articles: List<Article>
    private lateinit var existArticleId: UUID

    init {
        extension(SpringExtension)

        beforeSpec {
            member = createMember(Role.ADMIN)
            member = memberRepository.save(member)
            categories = createCategory(2)
            categories = categoryRepository.saveAll(categories)
            articles = createArticles(member, categories[0], 10)
                .plus(createArticles(member, categories[1], 10))

            articles = articleRepository.saveAll(articles)
        }

        Given("존재하는 게시글 ID가 주어진다") {
            val existId = articles[0].id
            When("게시물을 조회하면") {
                val articleDto = articleRepository.findByArticleId(existId)

                Then("ArticleDto가 반환된다") {
                    (articleDto is ArticleDto) shouldBe true
                    articleDto.id shouldBe articles[0].id
                    articleDto.contents shouldBe articles[0].contents
                    articleDto.title shouldBe articles[0].title
                    articleDto.author.id shouldBe articles[0].author.id
                    articleDto.category.id shouldBe articles[0].category.id
                }
            }
        }

        Given("존재하지 않는 게시물 ID가 주어진다") {
            val notExist = UUID.randomUUID()
            When("게시물을 조회하면") {
                Then("NotFoundException이 발생한다") {
                    shouldThrow<NotFoundException> {
                        articleRepository.findByArticleId(notExist)
                    }
                }
            }
        }

        Given("카테고리 ID가 0L로 주어진다") {
            val categoryId = 0L
            When("게시글을 15개 단위로 조회하면") {
                val result = articleRepository.findArticles(categoryId, null, 15)
                Then("전체 카테고리의 게시물 목록 15개가 반환된다") {
                    result.size shouldBe 15
                    result.forEach {
                        it.category.id shouldNotBe categoryId
                    }
                }
            }
        }

        Given("존재하는 카테고리 ID가 주어진다") {
            val existId = categories[0].id
            When("게시글을 15개 단위로 조회하면") {
                val result = articleRepository.findArticles(existId, null, 15)
                Then("해당 카테고리의 게시글이 10개 조회된다") {
                    result.size shouldBe 10
                    result.forEach{
                        it.category.id shouldBe existId
                        it.category.name shouldBe categories[0].name
                    }
                }
            }
        }

        Given("존재하지 않는 카테고리 ID가 주어진다") {
            val notExistId = 10000L
            When("게시글을 조회하면") {
                val result = articleRepository.findArticles(notExistId, null, 15)
                Then("빈 리스트가 반환된다") {
                    result.isEmpty() shouldBe true
                }
            }
        }

        Given("검색 커서가 주어진다") {
            val cursor = articles[10].createdAt
            When("커서를 주고 전체 게시물 목록을 조회하면") {
                val result = articleRepository.findArticles(0L, cursor, 15)
                Then("커서 이전의 게시글만 조회된다") {
                    result.isEmpty() shouldBe false
                    result.forEach {
                        it.createdAt shouldBeLessThanOrEqualTo cursor
                    }
                }
            }
        }

        afterSpec {
            articleRepository.deleteAll()
            categoryRepository.deleteAll()
            memberRepository.deleteAll()
        }
    }
}
