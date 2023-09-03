package kr.dohoonkim.blog.restapi.study

import io.kotest.core.spec.style.AnnotationSpec
import org.apache.commons.io.FilenameUtils
import java.net.URL
import javax.imageio.ImageIO

class ImageIoTest : AnnotationSpec() {
    @Test
    fun `확장자`() {
        val result = FilenameUtils.getExtension("testimage.png")
        println(result)
    }
}