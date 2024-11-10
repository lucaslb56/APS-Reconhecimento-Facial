package interfaces;

import facialRecognition.FaceDetection;
import org.opencv.core.Mat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

import static facialRecognition.ImageConverter.matToBufferedImage;

public class IVideoModal extends JDialog {
    private VideoPanel videoPanel;
    private Thread videoLoop;
    private FaceDetection faceScan;
    private Boolean detect;
    private Boolean capture;
    private JLabel labelMessage;
    private JButton captureButton;
    public Mat bioFacial;

    public IVideoModal(JFrame requesterUi, String title){
        super(requesterUi, title, true);
        setSize(640, 480);
        setLocationRelativeTo(requesterUi);
        setComponents();
    }

    public void open(){
        labelMessage.setText("Carregando video...");
        labelMessage.setForeground(Color.BLACK);
        videoLoop = new Thread(this::generateVideo);
        videoLoop.setDaemon(true);
        videoLoop.start();
        this.setVisible(true);
    }

    private void generateVideo(){
        faceScan = new FaceDetection();
        faceScan.openCamera(0);

        Mat frame = new Mat();
        labelMessage.setText("");
        while (faceScan.render(frame)) {
            // Validação do rosto
            faceValidation(frame);
            if (capture) captureBioFacial(frame);
            BufferedImage img = matToBufferedImage(frame);
            videoPanel.setImage(img);
            // redução da quantidade de loops
            try {
                Thread.sleep(30);  // Aproximadamente 30 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!this.isVisible())
                break;

        }
        faceScan.closeCamera();
    }

    private void setComponents(){
        videoPanel = new VideoPanel();
        detect = false;
        capture = false;
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        labelMessage = new JLabel("Carregando video...");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 16)); 
        controlPanel.add(labelMessage, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton detectionButton = new JButton("Iniciar detecção");
        captureButton = new JButton("Capturar");
        buttonPanel.add(captureButton);
        // buttonPanel.add(detectionButton);

        controlPanel.add(buttonPanel, BorderLayout.EAST);
        captureButton.addActionListener(e -> capture = true);
        detectionButton.addActionListener(e -> {
            if (detect) {
                detectionButton.setText("Iniciar detecção");
                detect = false;
            } else {
                detectionButton.setText("Parar detecção");
                detect = true;
            }
        });
        
        add(controlPanel, BorderLayout.SOUTH);
        add(videoPanel, BorderLayout.CENTER);
    }

    private void faceValidation(Mat frame){
        String validation = faceScan.faceValidation(frame);
        if (validation != labelMessage.getText()) {
            labelMessage.setText(validation);
            if (validation == "Pronto para captura!"){
                captureButton.setEnabled(true);
                labelMessage.setForeground(Color.BLUE);
            }else {
                captureButton.setEnabled(false);
                labelMessage.setForeground(Color.RED);
            }
        } 
        
    }

    private void captureBioFacial(Mat frame){
        try {
            labelMessage.setText("Capturando rosto...");
            bioFacial = faceScan.captureFace(frame);
            this.capture = false;
            this.dispose();
        } catch (Exception e) {
            this.labelMessage.setText(e.getMessage());
            this.capture = false;
        }
    }
}




