package kr.dohoonkim.blog.restapi.application.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
//import kr.dohoonkim.blog.restapi.domain.article.Comment
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import java.time.LocalDateTime

// TODO("comment 구현 후 재개")
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
//data class CommentDto(
//    val id : Long,
//    val parentId : Long = 0,
//    val member : MemberSummaryDto,
//    val contents : String,
//    val createdAt : LocalDateTime,
//    val repliesCount : Int,
//    val isMyComment : Boolean = true
//){
//    companion object {
//        fun fromEntity(comment : Comment) : CommentDto {
//            return CommentDto(
//                    id = comment.id,
//                    parentId = comment.parent?.id ?: 0,
//                    member = MemberSummaryDto.fromEntity(comment.author),
//                    contents = comment.contents,
//                    createdAt = comment.createdAt,
//                    repliesCount = comment.childrens.size
//            )
//        }
//    }
//}
