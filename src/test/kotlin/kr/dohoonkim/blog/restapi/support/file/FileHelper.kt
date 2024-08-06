package kr.dohoonkim.blog.restapi.support.file

import org.springframework.mock.web.MockMultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

fun createBufferedImage(
    width: Int,
    height: Int,
    format: Int = BufferedImage.TYPE_INT_ARGB)
: BufferedImage{
    return BufferedImage(width, height, format)
}

fun convertBufferedImageToInputStream(bufferedImage: BufferedImage, format: String="png")
: InputStream {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, byteArrayOutputStream)
    return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
}

fun createImageMockMultipartFile(name: String, format: String): MockMultipartFile {
    val bufferedImage = createBufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    byteArrayOutputStream.close()

    return MockMultipartFile(name, "$name.$format", "image/$format", byteArray)
}

fun createTextMockMultipartFile(name: String, contents: String): MockMultipartFile {
    return MockMultipartFile(name, "${name}.txt", "text/plain", contents.toByteArray())
}

