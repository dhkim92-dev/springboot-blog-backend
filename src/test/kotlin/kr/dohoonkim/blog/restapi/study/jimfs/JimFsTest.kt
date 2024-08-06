package kr.dohoonkim.blog.restapi.study.jimfs

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import io.kotest.core.spec.style.AnnotationSpec
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

class JimFsTest: AnnotationSpec() {
    private val fs = Jimfs.newFileSystem(Configuration.unix() )
    private val path: Path = fs.getPath("/virtual")

    fun createMockImage(): BufferedImage {
        val width = 100
        val height = 100
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.createGraphics()
        graphics.drawRect(10, 10, 80, 80)
        graphics.dispose()
        return image
    }

    @Test
    fun `write text to jimfs`() {
        println("Virtual Path: ${path.toAbsolutePath()}")

        Files.createDirectories(path)

        val tempFile = File.createTempFile("test", ".txt")
        tempFile.writeText("1234444444444")

        val targetFilePath = path.resolve("test.txt")
        println("targetFilePath : " + targetFilePath)

        Files.write(targetFilePath, tempFile.readBytes())

        assert(Files.exists(targetFilePath)) { "File should exist in the virtual file system" }

        val content = Files.readString(targetFilePath)
        assert(content == "1234444444444") { "File content should match the expected content" }
        println(content)
    }

    @Test
    fun `write image to jimfs`() {
        println("Virtual Path: ${path.toAbsolutePath()}")

        // Create directories in the virtual file system
        Files.createDirectories(path)

        // Create a mock image
        val image = createMockImage()
        val imageFormat = "png" // Image format
        val imageFileName = "test_image.png"

        // Write the image to a ByteArrayOutputStream
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, imageFormat, outputStream)
        val imageBytes = outputStream.toByteArray()

        // Define target file path in the virtual file system
        val targetFilePath = path.resolve(imageFileName)
        println("Target File Path: $targetFilePath")

        // Write the image bytes to the target file path
        Files.write(targetFilePath, imageBytes)

        // Verify the file exists
        assert(Files.exists(targetFilePath)) { "File should exist in the virtual file system" }

        // Read the file back and verify the content
        val readBytes = Files.readAllBytes(targetFilePath)
        assert(readBytes.contentEquals(imageBytes)) { "File content should match the expected content" }
        println("Image file written and verified successfully")
    }
}