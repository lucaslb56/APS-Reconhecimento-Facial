package facialRecognition;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;


import java.util.List;

public class FaceDetection {
    private Net faceNet;
    public FaceDetection() {
        // Configuração do modelo de rede neural para detecção de rostos
        String modelConfiguration = "res/Modelos_DNN/deploy.prototxt.txt";
        String modelWeights =  "res/Modelos_DNN/res10_300x300_ssd_iter_140000.caffemodel";
        faceNet = Dnn.readNetFromCaffe(modelConfiguration, modelWeights);
    }

    public String detectFaces(Mat frame){
        // Conversão da imagem em blob e processamento da mesma pela DDN
        Mat blobImage = Dnn.blobFromImage(frame, 1.0, new Size(300, 300), new Scalar(104.0, 177.0, 123.0), false, false);
        faceNet.setInput(blobImage);
        Mat detections = faceNet.forward();

        // Validação se houve apenas um rosto detectado
       /* if (detections.empty()) throw new RuntimeException("Nenhum rosto detectado!");
        else if (detections.rows() > 1) throw new RuntimeException("Muitos rostos detectados!");*/



            // alinhar imagem
            //Mat alignmentFace =  FaceAlignment.alignFace(frame, faceRect);
            // Obter vetor de caracteristicas
        drawRectImages(detections, frame);
        return "";

    }

    private void drawRectImages(Mat detections, Mat frame) {
        detections = detections.reshape(1, (int) detections.total() / 7);
        int cols = frame.cols();
        int rows = frame.rows();

        for (int l = 0; l < rows; l++) {
            double confidence = detections.get(l, 2)[0];
            if (confidence > 0.5) {
                // Criação de retângulo com as posições da detecção da imagem
                int x1 = (int) (detections.get(l, 3)[0] * cols);
                int y1 = (int) (detections.get(l, 4)[0] * rows);
                int x2 = (int) (detections.get(l, 5)[0] * cols);
                int y2 = (int) (detections.get(l, 6)[0] * rows);
                Rect faceRect = new Rect(new Point(x1, y1), new Point(x2, y2));

                // Desenho de retangulo em torno do rosto
                Imgproc.rectangle(frame, faceRect.tl(), faceRect.br(), new Scalar(0, 255, 0));
            }
        }
    }

    public List<Float> getVetorFace(Mat frame){
        Mat blobImage = Dnn.blobFromImage(frame, 1.0, new Size(224, 224), new Scalar(104.0, 177.0, 123.0), false, false);
        faceNet.setInput(blobImage);
        Mat featureMat = faceNet.forward();

        // Converter o Mat de características em um vetor de floats
        featureMat = featureMat.reshape(1, (int) featureMat.total() / 7);

        List<Float> featureVector = new ArrayList<>();
        for (int i = 0; i < featureMat.total(); i++) {
            featureVector.add((float) featureMat.get(i, 0)[0]);
        }

        return featureVector;
    }


    public void saveImage(Mat frame){
        String fileName = String.valueOf(frame.nativeObj);
        String filePath = "res\\imagens\\"+fileName+".png";
        Imgcodecs.imwrite(filePath, frame);
    }

}
