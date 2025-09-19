package kr.dohoonkim.blog.restapi.stash.domain.entity

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.entity.LongPrimaryKeyEntity

@Entity
@Table(name = "article_category")
class Category(id: Long) : LongPrimaryKeyEntity(id) {

    constructor(name: String): this(0L) {
        this.name = name
    }

    @Column(unique = true)
    var name: String = ""
        protected set

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var articles: MutableList<Article> = arrayListOf()
        protected set;

    fun addArticle(article: Article) {
        articles.add(article)
        article.updateCategory(this)
    }

    fun changeName(name: String) {
        this.name = name
    }
}