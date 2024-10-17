package facialRecognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageConverter {

    // Método que converte um Mat (OpenCV) em BufferedImage (Swing)
    public static BufferedImage matToBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_3BYTE_BGR; // Mantém a imagem colorida



        // Inverte a imagem horizontalmente (eixo X)
        Mat flippedMat = new Mat();
        Core.flip(matrix, flippedMat, 1); // 1 para inverter horizontalmente
        matrix = flippedMat;

        // Cria uma imagem BufferedImage do tamanho do Mat
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        // Cria um buffer de bytes para a imagem
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        // Copia os dados do Mat para o buffer
        matrix.get(0, 0, targetPixels);

        return image;
    }
}
