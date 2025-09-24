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

@Entity(name = "article")
class Article(
    @Id
    @GeneratedValue
    @UuidGenerator(algorithm = UUIDGenerator::class)
    val id: UUID? = null,
    title: String = "",
    contents: String = "",
    author: Member = Member(),
    category: Category = Category(),
) : BaseEntity() {

    companion object {
        fun create(
            member: Member,
            category: Category,
            title: String,
            contents: String,
        ): Article {
            require( member.isAdmin() ) { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }
            require( title.isNotBlank() ) { throw BadRequestException("제목은 비어 있을 수 없습니다.") }
            require( contents.isNotBlank() ) { throw BadRequestException("내용은 비어 있을 수 없습니다.") }
            category.increaseCount()

            return Article(
                title = title,
                contents = contents,
                author = member,
                category = category
            )
        }
    }

    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false, columnDefinition = "TEXT")
    var contents: String = contents
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var author: Member = author
        protected set

    // Cascade 옵션 필요 없음.
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "category_id")
    var category: Category = category
        protected set

    fun updateTitle(requester: Member, title: String?) {
        checkPermission(requester)
        this.title = title ?: this.title
    }

    fun updateCategory(requester: Member, category: Category?) {
        checkPermission(requester)
        require(category != null ) { return }
        if (this.category.id != category.id)
            this.category = category
    }

    fun updateContents(requester: Member, contents: String?) {
        checkPermission(requester)
        require ( !contents.isNullOrBlank() ) { return }
        this.contents = contents
    }

    private fun checkPermission(requester: Member) {
        require(requester.isAdmin()) { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }
    }

    override fun markAsDeleted() {
        super.markAsDeleted()
        this.category.decreaseCount()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Article) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
