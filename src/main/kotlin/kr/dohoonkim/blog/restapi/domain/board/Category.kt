package kr.dohoonkim.blog.restapi.domain.board

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.BadRequestException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.domain.BaseEntity
import kr.dohoonkim.blog.restapi.domain.member.Member

@Entity
@Table(name = "article_category")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    name: String = "",
    count: Long = 0
) : BaseEntity() {

    companion object {

        fun create(
            member: Member,
            name: String,
        ): Category {
            require( member.isAdmin() ) { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }
            require( name.isNotBlank() ) { throw BadRequestException("카테고리 이름은 비어있을 수 없습니다.") }

            return Category(
                name = name
            )
        }
    }

    @Column(unique = true)
    var name: String = name
        protected set

    @Column
    var count: Long = count
    protected set

    fun increaseCount() {
        this.count += 1L
    }

    fun decreaseCount() {
        if ( this.count > 0 ) {
            this.count -= 1L
        }
    }

    fun changeName(requester: Member, name: String?) {
        require( name != null) { return; }
        require( requester.isAdmin() ) { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }
        require(name.isNotBlank() ) { "카테고리 이름은 비어 있을 수 없습니다." }
        if ( this.name == name) return
        this.name = name
    }
}