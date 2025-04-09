package kr.dohoonkim.blog.restapi.application.file

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnsupportedMediaTypeException
import kr.dohoonkim.blog.restapi.common.utility.ImageResizer
import kr.dohoonkim.blog.restapi.common.utility.ImageTypeValidator
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

/**
 * 파일 업로드 서비스
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
class FileUploadService(
    private val imageTypeValidator: ImageTypeValidator,
    private val imageResizer: ImageResizer,
    private val host: String,
    private val basePath: Path
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 전송받은 이미지 파일을 저장한다
     * @param file 이미지 파일 MultipartFile
     * @return 저장된 이미지 접근 URI
     * @throws UnsupportedMediaTypeException 지원하지 않는 파일 형식
     */
    fun createImage(file: MultipartFile): String {

        logger.info("File upload request : ${file.originalFilename}")

        if (!imageTypeValidator.isSupportFile(file)) {
            logger.info("${file.originalFilename} is not supported media type.")
            throw UnsupportedMediaTypeException(ErrorCodes.NOT_SUPPORT_IMAGE_TYPE)
        }

        val ext = FilenameUtils.getExtension(file.originalFilename)
        val resizedImage = imageResizer.resize(file.inputStream, 1280)
        val filename = "${UUID.randomUUID().toString()}.$ext"
        val relativePath = "images/${filename[0]}/${filename}"
        val filePath = basePath.resolve("images/${filename[0]}/${filename}")
        logger.info("file path : ${filePath}")

        // 부모 경로 생성
        Files.createDirectories(filePath.parent)
        logger.info("${file.originalFilename} created parent directory.")

        // Image IO로 결과를 OutputStream에 작성
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(resizedImage, ext, outputStream)
        logger.info("${file.originalFilename} image writed.")
        val imageBytes = outputStream.toByteArray()

        // Files를 이용하여 저장
        Files.write(filePath, imageBytes)
        logger.info("${file.originalFilename} file writed.")

        return "$host/${relativePath}"
    }
}