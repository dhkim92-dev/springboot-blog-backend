package kr.dohoonkim.blog.restapi.port.persistence.board

import kr.dohoonkim.blog.restapi.domain.board.Comment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CommentRepository : JpaRepository<Comment, UUID>, CommentRepositoryCustom {

}