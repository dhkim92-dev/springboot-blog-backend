package kr.dohoonkim.blog.restapi.configs

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles

@TestConfiguration
//@EnableJpaRepositories
//@EnableJpaAuditing
@ActiveProfiles("test/kotlin/resources/application.yaml")
class TestJpaConfig(@PersistenceContext var em : EntityManager){
    @Bean
    fun jpaQueryFactory() : JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}
