package kr.dohoonkim.blog.restapi.units.common.utility

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.common.utility.ImageResizer
import kr.dohoonkim.blog.restapi.support.file.convertBufferedImageToInputStream
import kr.dohoonkim.blog.restapi.support.file.createBufferedImage
import java.awt.image.BufferedImage
import java.io.InputStream

internal class ImageResizerTest: AnnotationSpec() {

    private val imageResizer = ImageResizer()

    private lateinit var inputStream: InputStream

    private lateinit var bufferedImage: BufferedImage

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `이미지 크기를 비율을 유지하지 않고 변경 가능하다`() {
        bufferedImage = createBufferedImage(100, 100)
        inputStream = convertBufferedImageToInputStream(bufferedImage)

        val resizedImage = imageResizer.resize(inputStream, 120, 100)
        resizedImage.width shouldBe  120
        resizedImage.height shouldBe 100
        inputStream.close()
    }

    @Test
    fun `이미지 크기를 비율을 유지하고 변경 가능하다`() {
        bufferedImage = createBufferedImage(100, 100)
        inputStream = convertBufferedImageToInputStream(bufferedImage)

        val resizedImage = imageResizer.resize(inputStream, 50)
        resizedImage.width shouldBe  50
        resizedImage.height shouldBe 50
        inputStream.close()
    }

    @Test
    fun `이미지 크기를 비율을 유지하고 크기가 원본보다 크다면 변화하지 않는다`() {
        bufferedImage = createBufferedImage(100, 100)
        inputStream = convertBufferedImageToInputStream(bufferedImage)

        val resizedImage = imageResizer.resize(inputStream, 120)
        resizedImage.width shouldBe  100
        resizedImage.height shouldBe 100
        inputStream.close()
    }


    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}