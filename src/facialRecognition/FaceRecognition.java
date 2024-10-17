package facialRecognition;

import java.util.List;

public class FaceRecognition {

    // Limiar de similaridade para reconhecer os rostos
    private static final float SIMILARITY_THRESHOLD = 0.8f;

    // Função para calcular a distância Euclidiana entre dois vetores de características
    private static double calculateEuclideanDistance(List<Float> faceVetor1, List<Float> faceVetor2) {
        if (faceVetor1.size() != faceVetor2.size()) {
            throw new IllegalArgumentException("Os vetores de características devem ter o mesmo tamanho.");
        }

        double somaQuadrados = 0.0;
        for (int i = 0; i < faceVetor1.size(); i++) {
            double diferenca = faceVetor1.get(i) - faceVetor2.get(i);
            somaQuadrados += diferenca * diferenca;
        }

        return Math.sqrt(somaQuadrados);
    }

    // Realiza comparação dos vetores de rosto
    public static boolean recognizeFace(List<Float> faceVetor1, List<Float> faceVetor2) {
        double distance = calculateEuclideanDistance(faceVetor1, faceVetor2);
        System.out.println("Distância euclidiana: " + distance);

        return distance < SIMILARITY_THRESHOLD;
    }
}
