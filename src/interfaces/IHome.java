package interfaces;

import javax.swing.*;
import java.awt.*;

public class IHome extends JPanel {
    private MainWindow mainWindow;
    private JLabel contentLabel;
    public IHome(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        // Definindo preferências de tamanho e layout
        setPreferredSize(new Dimension(800, 700));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // -------------------Título-------------------
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Bem-vindo à Home", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // -------------------Conteúdo-------------------
        JPanel contentPanel = new JPanel();
        contentLabel = new JLabel("<html><div style='text-align: center;'>Esta é a página inicial.<br>Você pode navegar pelas opções disponíveis no menu.</div></html>", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPanel.add(contentLabel);

        // -------------------Adicionando os painéis à janela-------------------
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void show(){
        if (this.mainWindow.sessionUSer == null) this.mainWindow.tooglePanel(0);
        String content = "";
        switch (this.mainWindow.sessionUSer.getPermission()) {
            case 0:
                // Conteudo permissão usuário
                content = String.format("<html><div style='text-align: center;'>Esta é a página inicial.<br> Olá %s, você tem permissão de Usuário </div></html>", mainWindow.sessionUSer.getName());
                break;
            case 1:
                // Conteudo permissão diretor
                content = String.format("<html><div style='text-align: center;'>Esta é a página inicial.<br> Olá %s, você tem permissão de Diretor </div></html>", mainWindow.sessionUSer.getName());
                break;
            case 2:
                // Conteudo permissão ministro
                content = String.format("<html><div style='text-align: center;'>Esta é a página inicial.<br> Olá %s, você tem permissão de Ministro </div></html>", mainWindow.sessionUSer.getName());
                break;
            default:
                break;
        }
        contentLabel.setText(content);
    }
}