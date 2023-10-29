package kr.dohoonkim.blog.restapi.security

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

class MethodPathRequestMatcher() : RequestMatcher {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val methodPath: MutableMap<String, MutableSet<String>> = mutableMapOf()
    private val matchers: MutableMap<String, OrRequestMatcher> = mutableMapOf()

    fun add(method: HttpMethod, endpoints: Array<String>) {
        if (!methodPath.keys.contains(method.name())) {
            methodPath.put(method.name(), mutableSetOf(*endpoints));
        } else {
            methodPath["${method.name()}"]!!.addAll(endpoints)
        }
    }

    fun build() {
        methodPath.forEach { k, v ->
            val antMatchers = v.map { path ->
                AntPathRequestMatcher(path)
            }.toList()

            matchers.put(k, OrRequestMatcher(antMatchers))
        }
    }

    override fun matches(request: HttpServletRequest): Boolean {
        log.debug("request uri : ${request.requestURI}")
        println("request url : ${request.requestURI}")
        if (!matchers.keys.contains(request.method)) {
            log.debug("method not supported.")
            println("method not supported.")
            return false;
        }

        val result = matchers[request.method]!!.matches(request)
        log.debug("method ${request.method} ${request.requestURI} match result {}", result)
        println("method ${request.method} ${request.requestURI} match result ${result}")
        println(methodPath)
        return result
    }
}