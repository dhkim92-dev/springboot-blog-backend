package kr.dohoonkim.blog.restapi.configs

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
//
//@TestConfiguration
//class EmbeddedRedisConfig(private val redisServer : RedisServer = RedisServer(6379) ) {
//
//    @PostConstruct
//    fun start() {
//        redisServer.start()
//    }
//
//    @PreDestroy
//    fun stop() {
//        redisServer.stop()
//    }
//
//}