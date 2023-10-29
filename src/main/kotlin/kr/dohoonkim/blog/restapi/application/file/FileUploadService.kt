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

@Service
class FileUploadService(
        private val imageTypeValidator : ImageTypeValidator,
        private val imageResizer : ImageResizer
    ) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${server.media.host}")
    private lateinit var host : String;
    @Value("\${server.media.storagePath}")
    private lateinit var basePath : String

    fun createImage(file : MultipartFile) : String {
        if(!imageTypeValidator.isSupportFile(file)) {
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