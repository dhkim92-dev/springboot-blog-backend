package kr.dohoonkim.blog.restapi.support.entity

import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun createCategory(size: Int): List<Category> {
    return List(size) {
        val category = Category((it+1).toLong())
        category.createdAt = LocalDateTime.now()
        category.changeName("category-${it+1}")
        category.updateLastModifiedDate()
        category
    }
}

fun createCategory() : Category {
    return createCategory(1)[0]
}

fun createMember(size: Int): List<Member> {
    return List(size) {
        val member = Member(id = UUID.randomUUID())
        member.updatePassword(BCryptPasswordEncoder(10).encode("test1234"))
        member.updateEmail("email-${it}@dohoon-kim.kr")
        member.isActivated=true
        member.role = Role.MEMBER
        member.updateNickname("nickname-${it}")
        member
    }
}

fun createMember(role : Role = Role.MEMBER, activated : Boolean = true) : Member {
    val member = Member(
        nickname = UUID.randomUUID().toString().substring(0, 32),
        email = "${UUID.randomUUID().toString().substring(16)}@gmail.com",
        isActivated = activated,
        password = BCryptPasswordEncoder(10).encode("test1234")
    )

    member.updateRole(role)

    return member
}

fun createArticles(member: Member, category: Category, size: Int): List<Article> {
    return List<Article>(size) {
        val article = Article(
            author = member,
            category = category,
            title = "${category.name}-article-${it+1}",
            contents = "${category.name}-article-contents-${it+1}"
        )
        val instant = Instant.ofEpochMilli(System.currentTimeMillis() + it * 3600 * 1000)
        article.createdAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        article
    }
}

fun createArticle(member : Member, category: Category) = Article(author = member,
        title = UUID.randomUUID().toString(),
        contents = UUID.randomUUID().toString(),
        category = category
)

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