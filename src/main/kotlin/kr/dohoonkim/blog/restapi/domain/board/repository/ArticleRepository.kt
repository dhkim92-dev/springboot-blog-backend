package kr.dohoonkim.blog.restapi.domain.board.repository

import kr.dohoonkim.blog.restapi.domain.board.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ArticleRepository : JpaRepository<Article, UUID>, ArticleRepositoryCustom {

}