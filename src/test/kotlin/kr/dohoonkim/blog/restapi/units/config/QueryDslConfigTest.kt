package kr.dohoonkim.blog.restapi.units.config

import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import jakarta.persistence.EntityManager
import kr.dohoonkim.blog.restapi.config.QueryDslConfig

class QueryDslConfigTest: AnnotationSpec() {
    private lateinit var queryDslConfig: QueryDslConfig

    @BeforeEach
    fun setUp() {
        val entityManager = mockk<EntityManager>(relaxed = true)
        queryDslConfig = QueryDslConfig(entityManager)
    }

    @Test
    fun `jpaQueryFactory를 호출하면 JPAQueryFactory 객체가 반환된다`() {
        (queryDslConfig.jpaQueryFactory() is JPAQueryFactory) shouldBe true
    }


    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}