package facialRecognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.List;

public class FaceScan {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Carregar a biblioteca OpenCV
    }

    private VideoCapture camera;
    private FaceDetection faceDetection;

    public FaceScan() {
        faceDetection = new FaceDetection();
    }

    public Boolean scan(Mat frame) {
        try {
            faceDetection.detectFaces(frame);
            return true;
        }
        catch (Exception e) {return false; }

    }

    public List<Float> saveScan(Mat frame) {
        return faceDetection.getVetorFace(frame);
    }

    // Abrir a câmera
    public void openCamera(int indexCamera){
        camera = new VideoCapture(indexCamera);
        if (!camera.isOpened()) throw new IllegalStateException("A câmera não pôde ser aberta.");
    }

    // Fechar camera
    public void closeCamera() { camera.release(); }

    public Boolean capture(Mat frame) { return camera.read(frame); }







}
