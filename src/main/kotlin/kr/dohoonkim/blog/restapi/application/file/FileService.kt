package kr.dohoonkim.blog.restapi.application.file

import kr.dohoonkim.blog.restapi.application.file.dto.FileUploadResult
import kr.dohoonkim.blog.restapi.application.file.dto.UploadFileCommand
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.InternalServerError
import kr.dohoonkim.blog.restapi.config.FileServiceConfig
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import net.coobird.thumbnailator.Thumbnails
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

@Service
class FileService(
    private val memberRepository: MemberRepository,
    private val imageTypeValidator: ImageTypeValidator,
    private val fileServiceConfig: FileServiceConfig
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun uploadImage(loginId: UUID, command: UploadFileCommand): FileUploadResult {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        require(member.isAdmin()) { throw ForbiddenException(ErrorCodes.NO_PERMISSION) }

        val file = command.file
        val byteArray: ByteArray = file.inputStream.readBytes()
        val originalFileName = file.originalFilename ?: "unknown"
        imageTypeValidator.validate(byteArray)
        val basePath = fileServiceConfig.basePath
        val ext = originalFileName.substringAfterLast(".","")
        val filename = "${UUID.randomUUID()}.$ext"
        val relativePath = "images/${filename[0]}/$filename"
        val filePath = "$basePath/${relativePath}"
        val fileInputStream = ByteArrayInputStream(byteArray)
        val outputStream = ByteArrayOutputStream()
        val image = Thumbnails.of(fileInputStream)
            .size(1280, 720)
            .keepAspectRatio(true)
            .toOutputStream(outputStream)
        try {
            val savedFile = File(filePath)
            val parentDir = savedFile.parentFile
            if ( parentDir != null && !parentDir.exists() ) {
                parentDir.mkdirs()
            }
            savedFile.writeBytes(outputStream.toByteArray())
        } catch ( e:Exception ) {
            logger.error("파일 쓰기 실패 {}", e.message)
            throw InternalServerError(message = "파일 쓰기 실패")
        }

        return FileUploadResult(
            url = "${fileServiceConfig.host}/$filePath",
        )
    }
}