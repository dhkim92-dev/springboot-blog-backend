package kr.dohoonkim.blog.restapi.domain.article

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.entity.LongPrimaryKeyEntity

@Entity
@Table(name = "article_category")
class Category(name: String) : LongPrimaryKeyEntity() {

    @Column(unique = true)
    var name: String = name

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var articles: MutableList<Article> = arrayListOf()

    fun changeName(name: String) {
        this.name = name
    }

}