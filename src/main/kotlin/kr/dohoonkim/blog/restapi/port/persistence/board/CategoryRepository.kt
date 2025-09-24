package kr.dohoonkim.blog.restapi.port.persistence.board

import kr.dohoonkim.blog.restapi.domain.board.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<Category, Long> {

    fun existsByName(name: String): Boolean
}