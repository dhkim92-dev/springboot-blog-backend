package kr.dohoonkim.blog.restapi.common.utility

import org.springframework.stereotype.Component
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * Image 크기 변환 클래스
 */
@Component
class ImageResizer {

    /**
     * 이미지 비율을 신경쓰지 않고 강제로 지정한 크기로 변환시킨다.
     * @param inputStream - InputStream
     * @param width - 너비
     * @param height - 높이
     */

    fun resize(inputStream: InputStream, width: Int, height: Int): BufferedImage {
        val origin = ImageIO.read(inputStream)
        return createResizedBufferedImage(origin, width, height);
    }

    /**
     * 이미지의 비율을 유지하면서, 너비를 width 이하로 줄인다.
     * @param inputStream - InputStream
     * @param width : target width
     * @param ratio : target ratio
     */
    fun resize(inputStream: InputStream, width: Int): BufferedImage {
        val origin = ImageIO.read(inputStream)

        if (origin.width <= width) {
            return origin
        }

        val ratio = origin.height.toFloat() / origin.width.toFloat();
        val height = (ratio * width.toFloat()).toInt()

        return createResizedBufferedImage(origin, width, height)
    }

    private fun createResizedBufferedImage(origin: BufferedImage, width: Int, height: Int): BufferedImage {

        val resizedImage = origin.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        val outputBufferedImage = BufferedImage(width, height, origin.type);
        outputBufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

        return outputBufferedImage
    }

}