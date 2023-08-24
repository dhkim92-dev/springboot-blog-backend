package kr.dohoonkim.blog.restapi.application.file

import kr.dohoonkim.blog.restapi.common.utility.ImageResizer
import kr.dohoonkim.blog.restapi.common.utility.ImageTypeValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class FileUploadService(
        private val imageTypeValidator : ImageTypeValidator,
        private val imageResizer : ImageResizer
    ) {

    @Value("\${server.media}")
    private lateinit var MEDIA_PATH : String;

}