package kr.dohoonkim.blog.restapi.support.web

import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyArticleRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyCategoryRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostCategoryRequest

fun postArticleRequest(article: Article): PostArticleRequest {
    return PostArticleRequest(
        title = article.title,
        contents = article.contents,
        category = article.category.name
    )
}

fun postArticleRequest(articles: List<Article>): List<PostArticleRequest> {
    return articles.map {
        it -> PostArticleRequest(
            it.title,
            it.contents,
            it.category.name
        )
    }
}

fun modifyArticleRequest(article: Article): ModifyArticleRequest {
    return ModifyArticleRequest(article.title, article.contents, article.category.name)
}

fun postCategoryRequest(category: Category): PostCategoryRequest {
    return PostCategoryRequest(name = category.name)
}

fun modifyCategoryRequest(category: Category): ModifyCategoryRequest {
    return ModifyCategoryRequest(name = category.name)
}

