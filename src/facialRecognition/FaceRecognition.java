package facialRecognition;

import java.util.List;

public class FaceRecognition {

    // Limiar de similaridade para reconhecer os rostos
    private static final float LIMIAR_SIMILARIDADE = 0.5f; // Ajuste este valor conforme necessário

    // Função para calcular a distância Euclidiana entre dois vetores de características
    private static double calcularDistanciaEuclidiana(List<Float> vetor1, List<Float> vetor2) {
        if (vetor1.size() != vetor2.size()) {
            throw new IllegalArgumentException("Os vetores de características devem ter o mesmo tamanho.");
        }

        double somaQuadrados = 0.0;
        for (int i = 0; i < vetor1.size(); i++) {
            double diferenca = vetor1.get(i) - vetor2.get(i);
            somaQuadrados += diferenca * diferenca;
        }

        return Math.sqrt(somaQuadrados); // Retorna a raiz quadrada da soma dos quadrados
    }

    // Função para verificar se os dois vetores de características são similares (se o rosto é reconhecido)
    public static boolean reconhecerRosto(List<Float> vetor1, List<Float> vetor2) {
        double distancia = calcularDistanciaEuclidiana(vetor1, vetor2);
        System.out.println("Distância Euclidiana entre os vetores de características: " + distancia);

        // Se a distância for menor que o limiar, considera-se que o rosto foi reconhecido
        return distancia < LIMIAR_SIMILARIDADE;
    }

    public static void main(String[] args) {
        // Exemplo de dois vetores de características
        List<Float> vetorFace1 = List.of(1.2f, 3.4f, 5.6f, 7.8f); // Exemplo simplificado
        List<Float> vetorFace2 = List.of(1.1f, 3.5f, 5.7f, 7.9f); // Exemplo simplificado

        // Verifica se os dois rostos são similares
        boolean reconhecido = reconhecerRosto(vetorFace1, vetorFace2);

        if (reconhecido) {
            System.out.println("Rostos reconhecidos como sendo da mesma pessoa.");
        } else {
            System.out.println("Rostos não reconhecidos.");
        }
    }
}
