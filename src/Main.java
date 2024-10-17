import dataBase.UserModel;
import facialRecognition.FaceScan;
import facialRecognition.FaceRecognition;
import interfaces.IRegister;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IRegister iRegister = new IRegister();
        iRegister.setVisible(true);
        /*FaceScan faceScan = new FaceScan();
        List<Float> faceVetor1 = faceScan.scan();
        List<Float> faceVetor2 = faceScan.scan();

        if(FaceRecognition.recognizeFace(faceVetor1, faceVetor2)) System.out.println("Reconhecido");
        else System.out.println("Não reconhecido!");*/
    }
}
