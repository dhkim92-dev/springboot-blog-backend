package kr.dohoonkim.blog.restapi.domain.board.repository

import kr.dohoonkim.blog.restapi.domain.board.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long>, CategoryRepositoryCustom {

    fun findByName(name: String): Category?

}