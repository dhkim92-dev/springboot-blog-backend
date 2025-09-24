package kr.dohoonkim.blog.restapi.application.file.dto

import org.springframework.web.multipart.MultipartFile

data class UploadFileCommand(
    val file: MultipartFile
) {
}