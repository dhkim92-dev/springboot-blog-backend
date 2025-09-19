package kr.dohoonkim.blog.restapi.stash.domain.repository

import kr.dohoonkim.blog.restapi.stash.domain.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ArticleRepository : JpaRepository<Article, UUID>, ArticleRepositoryCustom {

}