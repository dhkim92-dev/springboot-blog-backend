package kr.dohoonkim.blog.restapi.common.utility

import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.interfaces.board.ArticleController
import kr.dohoonkim.blog.restapi.interfaces.authentication.AuthenticationController
import kr.dohoonkim.blog.restapi.interfaces.board.CategoryController
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class WarmupRunner(
    private val articleController: ArticleController,
    private val categoryController: CategoryController,
    private val authenticationController: AuthenticationController
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        warmupCategoryController()
        warmUpArticleController()
    }

    private fun warmUpArticleController() {
        try {
            articleController.getArticles(0L, null, 20)
        } catch (e: Exception) {
            // nothing to do
        }
    }

    private fun warmupCategoryController() {
        try {
            categoryController.getCategories(200)
        } catch (e: Exception) {
            // nothing to do
        }
    }
}