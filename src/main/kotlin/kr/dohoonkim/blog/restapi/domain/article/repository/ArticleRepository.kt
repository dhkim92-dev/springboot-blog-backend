package kr.dohoonkim.blog.restapi.domain.article.repository

import kr.dohoonkim.blog.restapi.domain.article.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ArticleRepository : JpaRepository<Article, UUID>, ArticleRepositoryCustom {

}