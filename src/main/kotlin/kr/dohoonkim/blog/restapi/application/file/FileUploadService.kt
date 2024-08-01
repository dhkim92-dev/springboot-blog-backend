package kr.dohoonkim.blog.restapi.application.file

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnsupportedMediaTypeException
import kr.dohoonkim.blog.restapi.common.utility.ImageResizer
import kr.dohoonkim.blog.restapi.common.utility.ImageTypeValidator
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * 파일 업로드 서비스
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
@Service
class FileUploadService(
    private val imageTypeValidator: ImageTypeValidator,
    private val imageResizer: ImageResizer
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${server.media.host}")
    private lateinit var host: String;

    @Value("\${server.media.storagePath}")
    private lateinit var basePath: String

    /**
     * 전송받은 이미지 파일을 저장한다
     * @param file 이미지 파일 MultipartFile
     * @return 저장된 이미지 접근 URI
     * @throws UnsupportedMediaTypeException 지원하지 않는 파일 형식
     */
    fun createImage(file: MultipartFile): String {
        if (!imageTypeValidator.isSupportFile(file)) {
            throw UnsupportedMediaTypeException(ErrorCode.NOT_SUPPORT_IMAGE_TYPE)
        }

        val ext = FilenameUtils.getExtension(file.originalFilename)
        val resizedImage = imageResizer.resize(file.inputStream, 1280)
        val filename = "${UUID.randomUUID().toString()}.$ext"
        val filePath = "images/${filename[0]}/$filename"
        val file = File("$basePath/${filePath}")

        file.parentFile.mkdirs()
        ImageIO.write(resizedImage, ext, file)

        return "$host/$filePath"
    }

}