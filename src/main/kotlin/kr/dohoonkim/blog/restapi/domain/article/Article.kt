package kr.dohoonkim.blog.restapi.domain.article

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.entity.UuidPrimaryKeyEntity
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.hibernate.annotations.ColumnDefault

@Entity(name="article")
class Article(title : String, contents : String, author : Member, category : Category, viewCount : Long=0) : UuidPrimaryKeyEntity() {

    @Column(nullable = false)
    var title : String = title

    @Column(nullable = false, columnDefinition = "TEXT")
    var contents : String = contents

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    var author : Member = author

    @Column
    var viewCount : Long = 0

    // TODO("댓글 기능 구현 후 재개")
//    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    var comments : MutableList<Comment> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category : Category = category

    // TODO("댓글 기능 구현 후 재개")
//    fun addComment(comment: Comment) {
//        comments.add(comment)
//    }

    fun updateTitle(title : String) {
        this.title = title
    }

    fun updateCategory(category : Category) {
        if(this.category.id != category.id)
            this.category = category
    }

    fun updateContents(contents : String) {
        this.contents = contents
    }

    fun increaseViewCount() = viewCount++

    fun decreaseViewCount() = viewCount--

}
