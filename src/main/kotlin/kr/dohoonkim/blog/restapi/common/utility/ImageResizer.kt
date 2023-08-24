package kr.dohoonkim.blog.restapi.common.utility

import org.springframework.stereotype.Component
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.nio.Buffer
import javax.imageio.ImageIO

@Component
class ImageResizer {

    /**
     * 이미지 비율을 신경쓰지 않고 강제로 지정한 크기로 변환시킨다.
     * @param inputStream - InputStream
     * @param width - 너비
     * @param height - 높이
     */

    fun resize(inputStream : InputStream, width : Int, height : Int) : Graphics2D {
        val origin = ImageIO.read(inputStream)
        return createGraphics2D(origin, width, height);
    }

    /**
     * 이미지의 비율을 유지하면서, 너비를 width 이하로 줄인다.
     * @param inputStream - InputStream
     * @param width : target width
     * @param ratio : target ratio
     */
    fun resize(inputStream: InputStream, width : Int) : Graphics2D {
        val origin = ImageIO.read(inputStream)
        val ratio = origin.width as Float / origin.height as Float;
        val height = (ratio * width) as Int;
        return createGraphics2D(origin, width, height);
    }

    private fun createGraphics2D(origin : BufferedImage, width : Int, height : Int) : Graphics2D {
        val resizedImage = BufferedImage(width, height, origin.type)
        val graphics = resizedImage.createGraphics();
        graphics.drawImage(origin, 0, 0, width, height, null);
        graphics.dispose();

        return graphics;
    }

}