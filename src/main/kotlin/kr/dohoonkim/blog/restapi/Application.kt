package kr.dohoonkim.blog.restapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans

@SpringBootApplication
@EnableCaching
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

