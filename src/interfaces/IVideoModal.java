package interfaces;

import facialRecognition.FaceScan;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static facialRecognition.ImageConverter.matToBufferedImage;

public class IVideoModal extends JDialog {
    private VideoPanel videoPanel;
    private Thread videoLoop;
    private FaceScan faceScan;

    public IVideoModal(JFrame requesterUi, String title){
        super(requesterUi, title, true);
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
        while (faceScan.capture(frame)) {
            faceScan.scan(frame);
            BufferedImage img = matToBufferedImage(frame);
            videoPanel.setImage(img);

            // redução da quantidade de loops
            try {
                Thread.sleep(30);  // Aproximadamente 30 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Fechar o modal ao clicar no "X"
            if (!this.isVisible())
                break;

        }
        faceScan.closeCamera();
    }

    private void setComponents(){
        videoPanel = new VideoPanel();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Alinha o botão à direita

        // Botão de fechar
        JButton closeButton = new JButton("Fechar");
        buttonPanel.add(closeButton);

        // Adiciona o painel de botões na parte inferior (SOUTH) do BorderLayout
        add(buttonPanel, BorderLayout.SOUTH);
        add(videoPanel, BorderLayout.CENTER);
    }
}




