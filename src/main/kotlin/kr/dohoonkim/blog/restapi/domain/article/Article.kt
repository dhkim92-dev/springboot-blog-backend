package kr.dohoonkim.blog.restapi.domain.article

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.entity.UuidPrimaryKeyEntity
import kr.dohoonkim.blog.restapi.domain.member.Member

@Entity(name = "article")
class Article(title: String, contents: String, author: Member, category: Category, viewCount: Long = 0) :
    UuidPrimaryKeyEntity() {

    @Column(nullable = false)
    var title: String = title

    @Column(nullable = false, columnDefinition = "TEXT")
    var contents: String = contents

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var author: Member = author

    @Column
    var viewCount: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category = category

    fun updateTitle(title: String?) {
        this.title = title ?: this.title
    }

    fun updateCategory(category: Category?) {
        if(category == null) return

        if (this.category.id != category.id)
            this.category = category
    }

    fun updateContents(contents: String?) {
        this.contents = contents ?: this.contents
    }

    fun increaseViewCount() = viewCount++

    fun decreaseViewCount() = viewCount--

}
