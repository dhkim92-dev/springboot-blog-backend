package kr.dohoonkim.blog.restapi.config

import kr.dohoonkim.blog.restapi.application.file.FileUploadService
import kr.dohoonkim.blog.restapi.common.utility.ImageResizer
import kr.dohoonkim.blog.restapi.common.utility.ImageTypeValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.io.path.Path

@Configuration
class FileServiceConfig(
    @Value("\${server.media.host}")
    private val host: String,
    @Value("\${server.media.storagePath}")
    private val storagePath: String,
    private val imageResizer: ImageResizer,
    private val imageTypeValidator: ImageTypeValidator
) {

    @Bean
    fun fileUploadService() : FileUploadService {
        val basePath = Path(storagePath)
        return FileUploadService(imageTypeValidator, imageResizer, host, basePath)
    }
}