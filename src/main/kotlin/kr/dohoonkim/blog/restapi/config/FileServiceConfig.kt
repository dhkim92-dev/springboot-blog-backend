package kr.dohoonkim.blog.restapi.config
//
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FileServiceConfig(
    @Value("\${server.media.host}")
    val host: String,
    @Value("\${server.media.path}")
    val basePath: String,
) {

}