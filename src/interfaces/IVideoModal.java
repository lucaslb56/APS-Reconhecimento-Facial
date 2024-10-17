package interfaces;

import facialRecognition.FaceScan;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

import static facialRecognition.ImageConverter.matToBufferedImage;

public class IVideoModal extends JDialog {
    private VideoPanel videoPanel;
    private Thread videoLoop;
    private FaceScan faceScan;
    private Boolean detect;
    private JLabel labelMessage;

    public IVideoModal(JFrame requesterUi, String title){
        super(requesterUi, title, true);
        detect = false;
        setSize(640, 480);
        setLocationRelativeTo(requesterUi);
        setComponents();
    }

    public void open(){
        videoLoop = new Thread(()->generateVideo());
        videoLoop.setDaemon(true);
        videoLoop.start();
        this.setVisible(true);
    }

    private void generateVideo(){
        faceScan = new FaceScan();
        faceScan.openCamera(0);

        Mat frame = new Mat();
        labelMessage.setText("");
        while (faceScan.capture(frame)) {
            if (detect) faceScan.scan(frame);
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

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        labelMessage = new JLabel("Carregando video...");
        controlPanel.add(labelMessage, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton detectionButton = new JButton("Iniciar detecção");
        JButton captureButton = new JButton("Capturar");
        buttonPanel.add(detectionButton);
        buttonPanel.add(captureButton);

        controlPanel.add(buttonPanel, BorderLayout.EAST);

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
}




