package kr.dohoonkim.blog.restapi.units.common.utility

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.clearAllMocks
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.common.utility.WarmupRunner
import kr.dohoonkim.blog.restapi.interfaces.authentication.AuthenticationController
import kr.dohoonkim.blog.restapi.interfaces.board.ArticleController
import kr.dohoonkim.blog.restapi.interfaces.board.CategoryController
import org.springframework.boot.ApplicationArguments

internal class WarmupRunnerTest: AnnotationSpec() {

    private lateinit var articleController: ArticleController
    private lateinit var categoryController: CategoryController
    private lateinit var authenticationController: AuthenticationController
    private lateinit var warmupRunner: WarmupRunner

    @BeforeEach
    fun setUp() {
        articleController = mockk()
        categoryController = mockk()
        authenticationController = mockk()

        warmupRunner= WarmupRunner(articleController,
            categoryController,
            authenticationController
        )
    }

    @Test
    fun `run이 정상적으로 실행된다`() {
        shouldNotThrowAny {
            warmupRunner.run(null)
        }
    }

    @AfterEach
    fun clear() {
        clearAllMocks()
    }
}