package kr.dohoonkim.blog.restapi.domain.board

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.BadRequestException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.generator.UUIDGenerator
import kr.dohoonkim.blog.restapi.domain.BaseEntity
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity(name ="article_comment")
class Comment private constructor(
    @Id
    @GeneratedValue
    @UuidGenerator(algorithm = UUIDGenerator::class)
    val id: UUID? = null,
    writer: Member = Member(id = null, nickname = "탈퇴한 회원"),
    article: Article = Article(),
    parent: Comment? = null,
    content: String = "",
    depth: Int = 0,
    replyCount: Int = 0,
): BaseEntity() {

    companion object {

        fun create(
            writer: Member,
            article: Article,
            parent: Comment? = null,
            content: String,
        ): Comment {
            require(content.isNotBlank()) { throw BadRequestException("댓글 내용은 비어 있을 수 없습니다.") }
            require(content.length <= 500) { throw BadRequestException("메시지 길이는 500자를 넘을 수 없습니다.") }
            require( !writer.isBlocked ) { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }

            val rootComment = parent?.parent ?: parent
            rootComment?.increaseReplyCount()

            // 댓글의 깊이는 0부터 시작.
            // 댓글의 깊이는 최대 1
            // 2단계 이상의 댓글은 2단계로 고정
            // depth 0 : 댓글
            // depth 1 : 대댓글
            return Comment(
                writer = writer,
                article = article,
                parent = rootComment, // 2단계 이상 댓글은 2단계로 고정
                content = content,
                depth = ((parent?.depth ?: -1) + 1),
                replyCount = 0
            )
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var writer: Member = writer
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    var article: Article = article
        protected set

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "parent_id", nullable = true)
    var parent: Comment? = parent
        protected set

    @Column(length = 500, nullable = false)
    var content: String = content
        protected set

    @Column
    var depth: Int = depth
        protected set

    @Column
    var replyCount: Int = replyCount
        protected set

    fun increaseReplyCount() {
        this.replyCount += 1
    }

    fun decreaseReplyCount() {
        if ( this.replyCount > 0 ) {
            this.replyCount -= 1
        }
    }

    fun updateContent(requester: Member, contents: String?) {
        require( !requester.isBlocked )  { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }
        require(!contents.isNullOrBlank()) { return }
        require(this.writer.id == requester.id) { throw ForbiddenException(ErrorCodes.NO_PERMISSION.code, "작성자만 수정 가능합니다.") }
        require ( this.content != contents ) { return }
        require(contents.length <= 500) { throw BadRequestException("메시지 길이는 500자를 넘을 수 없습니다.") }

        this.content = contents
    }

    override fun markAsDeleted() {
        super.markAsDeleted()
        this.parent?.decreaseReplyCount()
    }
}
