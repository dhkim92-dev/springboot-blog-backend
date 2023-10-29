package kr.dohoonkim.blog.restapi.domain.article

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import kr.dohoonkim.blog.restapi.common.entity.LongPrimaryKeyEntity
import kr.dohoonkim.blog.restapi.domain.member.Member

@Entity(name = "article_comment")
class Comment(parent: Comment?, contents: String, author: Member, article: Article) : LongPrimaryKeyEntity() {

    @Column(nullable = false, columnDefinition = "TEXT")
    var contents: String = contents

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var author: Member = author

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    var article: Article = article

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    var childrens: MutableList<Comment> = mutableListOf()

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Comment? = null

    fun updateContents(contents: String) {
        this.contents = contents
    }

}