package facialRecognition;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;


import java.util.List;

public class FaceDetection {
    private Net faceDDN;
    public FaceDetection() {
        // Configuração do modelo de rede neural para detecção de rostos
        String modelConfiguration = "C:/Users/lucas/Desenvolvimentos/APS-Reconhecimento_Facial/src/Modelos_DNN/deploy.prototxt.txt";
        String modelWeights =  "C:/Users/lucas/Desenvolvimentos/APS-Reconhecimento_Facial/src/Modelos_DNN/res10_300x300_ssd_iter_140000.caffemodel";
        faceDDN = Dnn.readNetFromCaffe(modelConfiguration, modelWeights);
    }

    public List<Float> detectFace(Mat frame){
        // Conversão da imagem em blob e processamento da mesma pela DDN
        Mat blobImage = Dnn.blobFromImage(frame, 1.0, new Size(300, 300), new Scalar(104.0, 177.0, 123.0), false, false);
        faceDDN.setInput(blobImage);
        Mat detections = faceDDN.forward();

        // Validação se houve apenas um rosto detectado
        if (detections.empty()) throw new RuntimeException("Nenhum rosto detectado!");
        else if (detections.rows() > 1) throw new RuntimeException("Muitos rostos detectados!");

        // Tratamento da detecção
        detections = detections.reshape(1, (int) detections.total() / 7);
        int cols = frame.cols();
        int rows = frame.rows();

        // Teste da confiança da detecção
        double confidence = detections.get(0, 2)[0];
        if(confidence > 0.5) {
            // Criação de retângulo com as posições da detecção da imagem
            int x1 = (int) (detections.get(0, 3)[0] * cols);
            int y1 = (int) (detections.get(0, 4)[0] * rows);
            int x2 = (int) (detections.get(0, 5)[0] * cols);
            int y2 = (int) (detections.get(0, 6)[0] * rows);
            Rect faceRect = new Rect(new Point(x1, y1), new Point(x2, y2));

            // Desenho de retângulo na imagem
            Imgproc.rectangle(frame, faceRect.tl(), faceRect.br(), new Scalar(0, 255, 0));

            // alinhar imagem
            Mat alignmentFace =  FaceAlignment.alignFace(frame, faceRect);
            // Obter vetor de caracteristicas

            return getDescriptorVector(alignmentFace);
        } else throw new RuntimeException("Detecção com baixa confiabilidade!");
    }

    private List<Float> getDescriptorVector(Mat faceROI){
        // Inicializar ORB (detector de características)
        ORB orb = ORB.create();
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        Mat descriptors = new Mat();

        // Detectar características e computar os descritores na região do rosto
        orb.detectAndCompute(faceROI, new Mat(), keypoints, descriptors);

        // Verificar se as características foram detectadas
        if (!descriptors.empty()) {
            // Converter o Mat de descritores para um vetor de floats
            List<Float> descriptorVector = new ArrayList<>();
            for (int i = 0; i < descriptors.rows(); i++) {
                for (int j = 0; j < descriptors.cols(); j++) {
                    descriptorVector.add((float) descriptors.get(i, j)[0]);
                }
            }
            // Exibir o vetor de características
            System.out.println("Vetor de características do rosto: " + descriptorVector);
            return descriptorVector;
        }
        else throw new RuntimeException("Não foi possível criar vetor de caracteristicas!");
    }

}
