package facialRecognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.util.List;

public class FaceScan {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Carregar a biblioteca OpenCV
    }

    public FaceScan() {

    }

    public String scan() {
        FaceDetection faceDetection = new FaceDetection();
        Mat frame = capture(0);
        saveImagem(frame);
        List<Float> descriptorVector = faceDetection.detectFace(frame);
        return "";
    }

    public Mat capture(int indexCamera) {
        // Abrir a câmera
        VideoCapture camera = new VideoCapture(indexCamera);
        if (!camera.isOpened()) throw new IllegalStateException("A câmera não pôde ser aberta.");
        // Capturar um quadro da câmera
        Mat frame = new Mat();
        if (!camera.read(frame)) throw new RuntimeException("Não foi possível capturar imagem!.");
        camera.release();
        return frame;
    }

    public void saveImagem(Mat frame){
        String fileName = String.valueOf(frame.nativeObj);
        String filePath = "C:\\Users\\lucas\\Desenvolvimentos\\APS Reconhecimento Facial\\res\\imagens\\"+fileName+".png";
        Imgcodecs.imwrite(filePath, frame);
    }


}
