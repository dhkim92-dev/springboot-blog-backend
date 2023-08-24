package kr.dohoonkim.blog.restapi.support.entity

import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.domain.article.Category
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import java.util.*

fun createMember(role : Role = Role.MEMBER, activated : Boolean = true) : Member {
    val member = Member(
        nickname = UUID.randomUUID().toString().substring(0, 32),
        email = "${UUID.randomUUID().toString().substring(16)}@gmail.com",
        isActivated = activated,
        password = "1234abcd"
    )

    member.updateRole(role)

    return member
}

fun createCategory() : Category {
    val category = Category(name = UUID.randomUUID().toString().substring(0,15))
    category.id = 1L
    return category
}

fun createArticle(member : Member, category: Category) = Article(author = member,
        title = UUID.randomUUID().toString(),
        contents = UUID.randomUUID().toString(),
        category = category)

fun createDummyArticles(member : Member, category: Category, count : Long = 10, delay : Long = 50L) : List<Article> {
    val data : MutableList<Article> = mutableListOf()

    for(i in 1 .. count) {
        data.add(createArticle(member, category))
        Thread.sleep(delay)
    }

    data.sortedBy {
        it.createdAt
    }

    return data.reversed()
}