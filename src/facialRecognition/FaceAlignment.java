package facialRecognition;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceAlignment {
    private static CascadeClassifier eyeCascade = new CascadeClassifier("res\\Modelo_Alinhamento\\haarcascade_eye.xml");
    private static CascadeClassifier mouthCascade = new CascadeClassifier("res\\Modelo_Alinhamento\\haarcascade_mcs_mouth.xml");

    public static Mat alignFace(Mat frame, Rect faceRect) {

        Mat face = new Mat(frame, faceRect);

        MatOfRect eyes = new MatOfRect();
        eyeCascade.detectMultiScale(face, eyes);

        MatOfRect mouth = new MatOfRect();
        mouthCascade.detectMultiScale(face, mouth);

        if(eyes.toArray().length >= 2 && mouth.toArray().length >= 1) {

            Rect eye1 = eyes.toArray()[0];
            Rect eye2 = eyes.toArray()[1];

            Point eye1Center = new Point(eye1.x + eye1.width * 0.5, eye1.y + eye1.height * 0.5);
            Point eye2Center = new Point(eye2.x + eye2.width * 0.5, eye2.y + eye2.height * 0.5);

            if(eye1Center.x > eye2Center.x) {

                Point temp = eye1Center;
                eye1Center = eye2Center;
                eye2Center = temp;

            }

            double dx = eye2Center.x - eye1Center.x;
            double dy = eye2Center.y - eye1Center.y;
            double angle = Math.atan2(dy, dx) * 180 / Math.PI;

            if(Math.abs(angle) > 4) {

                angle = 0;

            }

            Point center = new Point((eye1Center.x + eye2Center.x) / 2, (eye1Center.y + eye2Center.y) / 2);

            Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0);

            Mat Face_Alinhada = new Mat();
            Imgproc.warpAffine(face, Face_Alinhada, rotationMatrix, face.size());

            Imgproc.resize(Face_Alinhada, Face_Alinhada, new Size(134, 134));

            return Face_Alinhada;

        }

        return face;

    }
}
