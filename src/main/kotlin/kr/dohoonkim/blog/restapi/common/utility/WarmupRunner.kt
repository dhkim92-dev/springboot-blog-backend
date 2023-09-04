package kr.dohoonkim.blog.restapi.common.utility

import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.interfaces.ArticleController
import kr.dohoonkim.blog.restapi.interfaces.AuthenticationController
import kr.dohoonkim.blog.restapi.interfaces.CategoryController
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class WarmupRunner(private val articleController: ArticleController,
                   private val categoryController: CategoryController,
                   private val authenticationController: AuthenticationController)
    : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        warmupCategoryController()
        warmUpArticleController()
        warmUpAuthenticationController()
    }

    private fun warmUpArticleController() {
        try {
            articleController.getArticles(0L, null)
        }catch(e : Exception) {
          // nothing to do
        }
    }

    private fun warmUpAuthenticationController() {
        try {
            val authenticationDto = LoginRequest(email = "test@gmail.com", password = "testpassword")
            authenticationController.login(authenticationDto)
        }catch(e : Exception) {
            // nothing to do
        }
    }

    private fun warmupCategoryController() {
        try {
            categoryController.getCategories()
        }catch(e : Exception) {
            // nothing to do
        }
    }
}