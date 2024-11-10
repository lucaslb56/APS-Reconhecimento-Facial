package facialRecognition;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import authentication.User;

import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.List;

public class FaceDetection {
    private Net faceNet;
    private VideoCapture camera;
    private int xMold1;
    private int yMold1;
    private int xMold2;
    private int yMold2;
    private int moldCenter;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Carregar a biblioteca OpenCV
    }

    public FaceDetection() {
        // Configuração do modelo de rede neural para detecção de rostos
        xMold1 = 220; 
        yMold1 = 100;
        xMold2 = 440; 
        yMold2 = 380;  
        moldCenter = (xMold1+xMold2)/2;
        String modelConfiguration = "res/Modelos_DNN/deploy.prototxt.txt";
        String modelWeights =  "res/Modelos_DNN/res10_300x300_ssd_iter_140000.caffemodel";
        faceNet = Dnn.readNetFromCaffe(modelConfiguration, modelWeights);
    }

    // Renderização da câmera com moldura pra rosto
    public Boolean render(Mat frame) { 
        if(camera.read(frame)){
            int lineSize =40;
            // Imgproc.rectangle(frame, new Point(xMold1, yMold1), new Point(xMold2, yMold2), new Scalar(0, 165, 255), 1);
            Imgproc.line(frame, new Point(xMold1, yMold1), new Point(xMold1+lineSize, yMold1),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold1, yMold1), new Point(xMold1, yMold1+lineSize),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold2, yMold1), new Point(xMold2-lineSize, yMold1),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold2, yMold1), new Point(xMold2, yMold1+lineSize),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold2, yMold2), new Point(xMold2-lineSize, yMold2),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold2, yMold2), new Point(xMold2, yMold2-lineSize),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold1, yMold2), new Point(xMold1+lineSize, yMold2),  new Scalar(0, 165, 255), 2);
            Imgproc.line(frame, new Point(xMold1, yMold2), new Point(xMold1, yMold2-lineSize),  new Scalar(0, 165, 255), 2);
            return true;
        } else return false;  
    }

    // Algoritimo de validação do rosto
    public String faceValidation(Mat frame) {
        Mat detections = getFacesDetections(frame);
        detections = detections.reshape(1, (int) detections.total() / 7);
        // Calculo dos pontos x e y do rosto
        int cols = frame.cols();
        int rows = frame.rows();
        int x1 = (int) (detections.get(0, 3)[0] * cols);
        int y1 = (int) (detections.get(0, 4)[0] * rows);
        int x2 = (int) (detections.get(0, 5)[0] * cols);
        int y2 = (int) (detections.get(0, 6)[0] * rows);
        // Calculo da largura e altura do rosto
        int width = x2-x1;
        int heigth = y2-y1;
        // Calculo do centro do rosto
        int faceCenter = (x1+x2) / 2;
        // Calculo da confiança do rosto
        double confidence = detections.get(0, 2)[0];
        // Valida se um rosto confiavel foi detectado
        if (confidence < 0.85) return "Nenhum rosto detectado!";
        // Valida distância do rosto em relação a camera
        if (width < 150 || heigth < 200) return "Aproxime o rosto da câmera...";
        // Valida se o rosto está corretamente posicionado
        if (x1 < xMold1 || x2 > xMold2 || y1 < yMold1 || y2 > yMold2) return "Alinhe corretamente o rosto na moldura...";
        // Valida o ângulo do rosto
        if (((heigth-width) < 40) || Math.abs(faceCenter-moldCenter) > 5) return "Alinhe o ângulo do rosto em relação a câmera...";
        // Rosto pronto
        return "Pronto para captura!";
    }

    // captura e salva um rosto em um frame
    public Mat captureFace(Mat frame){
        Mat detections = getFacesDetections(frame);
        detections = detections.reshape(1, (int) detections.total() / 7);
        // Validação da confiança da captura
        double confidence = detections.get(0, 2)[0];
        if (confidence <= 0.9) throw new RuntimeException("Detecção obteve baixa confiabilidade! posicione melhor o rosto.");
        // Criação de retângulo com as posições da detecção da imagem
        int cols = frame.cols();
        int rows = frame.rows();
        int x1 = (int) (detections.get(0, 3)[0] * cols);
        int y1 = (int) (detections.get(0, 4)[0] * rows);
        int x2 = (int) (detections.get(0, 5)[0] * cols);
        int y2 = (int) (detections.get(0, 6)[0] * rows);
        Rect faceRect = new Rect(new Point(x1, y1), new Point(x2, y2));
        // Ajustes, cortes e alinhamentos da imagem do rosto
        return FaceAlignment.alignFace(frame, faceRect);
    }

    // Detecção de rostos pela rede neural
    private Mat getFacesDetections(Mat frame){
        Mat blobImage = Dnn.blobFromImage(frame, 1.0, new Size(300, 300), new Scalar(104.0, 177.0, 123.0), false, false);
        faceNet.setInput(blobImage);
        return faceNet.forward();
    }

    // salva um frame como arquivo
    public String saveImage(Mat frame){
        String saveDir = "res/rostos/";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = saveDir + "face_" + timestamp + ".jpg";
        Imgcodecs.imwrite(fileName, frame);
        return fileName;
    }

    // Ler imagem salva
    public Mat loadImage(String fileName){
        return Imgcodecs.imread(fileName);
    }

    // compara rostos
    public Boolean compareFaces(Mat savedFace, Mat currentFace) {
        Mat hist1 = new Mat();
        Mat hist2 = new Mat();
        if (savedFace == null) throw new RuntimeException("O Arquivo da biometria facial deste usuário não existe ou está corrompido!");
        // Calcula histogramas para as imagens em escala de cinza
        try {
            Imgproc.cvtColor(savedFace, savedFace, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(currentFace, currentFace, Imgproc.COLOR_BGR2GRAY);
        } catch (Exception e) {
        }
        Imgproc.calcHist(List.of(savedFace), new MatOfInt(0), new Mat(), hist1, new MatOfInt(256), new MatOfFloat(0, 256));
        Imgproc.calcHist(List.of(currentFace), new MatOfInt(0), new Mat(), hist2, new MatOfInt(256), new MatOfFloat(0, 256));
        // Normaliza os histogramas
        Core.normalize(hist1, hist1);
        Core.normalize(hist2, hist2);
        // Calcula a correlação entre os histogramas
        Double similarity = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_CORREL);
        if (similarity > 0.85) return true;
        else return false;
    }

    // Verifica se um rosto existe no banco
    public Boolean isFaceRegistered(Mat face, List<User> users){
        for (User user : users){
            Mat savedFace = loadImage(user.getBioFacial());
            if (savedFace == null) continue;
            if (compareFaces(face, savedFace)) return true;
        }
        return false;
    }

    // Abrir a câmera
    public void openCamera(int indexCamera){
        camera = new VideoCapture(indexCamera);
        if (!camera.isOpened()) throw new IllegalStateException("A câmera não pôde ser aberta.");
    }

    // Fechar camera
    public void closeCamera() { camera.release(); }

}
