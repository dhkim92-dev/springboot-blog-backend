package kr.dohoonkim.blog.restapi.common.utility

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageTypeValidator {
    private val mimeTypes: List<String> = listOf("image/jpeg", "image/png", "image/jpg")

    fun isSupportFile(file: MultipartFile): Boolean {
//        println("fileName : ${file.originalFilename} format : ${file.contentType}")
        return mimeTypes.contains(file.contentType);
    }
}