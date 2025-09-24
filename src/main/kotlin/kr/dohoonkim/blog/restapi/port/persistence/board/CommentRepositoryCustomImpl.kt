package kr.dohoonkim.blog.restapi.port.persistence.board

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.dohoonkim.blog.restapi.domain.board.QArticle
import kr.dohoonkim.blog.restapi.domain.board.QComment
import kr.dohoonkim.blog.restapi.domain.member.QMember
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.port.persistence.board.dto.CommentQueryModel
import java.util.UUID

class CommentRepositoryCustomImpl(
    private val qf: JPAQueryFactory
) : CommentRepositoryCustom {

    override fun getCommentRepliesWithCursor(
        commentId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentQueryModel> {
        val rootComment = QComment.comment
        val replies = QComment("replies")
        val writer = QMember.member

        return qf.select(Projections.constructor<CommentQueryModel>(
            CommentQueryModel::class.java,
            replies.id,
            replies.parent.id,
            replies.article.id,
            Projections.constructor<MemberSummaryDto>(MemberSummaryDto::class.java,
                replies.writer.id,
                replies.writer.nickname,
            ),
            replies.content,
            replies.createdAt,
        ))
        .from(replies)
        .leftJoin(replies.parent, rootComment)
        .leftJoin(replies.writer, writer)
        .where(
            rootComment.id.eq(commentId),
            cursorCondition(replies, cursor)
        )
        .orderBy(replies.id.desc())
        .limit(size.toLong())
        .fetch()
    }

    override fun getPostCommentsWithCursor(
        postId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentQueryModel> {
        val post = QArticle.article
        val writer = QMember.member
        val comment = QComment.comment

        return qf.select(Projections.constructor<CommentQueryModel>(
            CommentQueryModel::class.java,
            comment.id,
            comment.parent.id,
            comment.article.id,
            Projections.constructor<MemberSummaryDto>(MemberSummaryDto::class.java,
                comment.writer.id,
                comment.writer.nickname,
            ),
            comment.content,
            comment.createdAt,
        ))
        .from(comment)
        .leftJoin(comment.article, post)
        .leftJoin(comment.writer, writer)
        .where(
            post.id.eq(postId),
            comment.parent.isNull,
            cursorCondition(comment, cursor)
        )
        .orderBy(comment.id.desc())
        .limit(size.toLong())
        .fetch()
    }

    fun cursorCondition(comment: QComment, cursor: UUID?): BooleanExpression? {
        return if ( cursor == null ) null
        else comment.id.loe(cursor)
    }
}