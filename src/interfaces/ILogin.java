package interfaces;

import javax.swing.*;

import authentication.User;
import dataBase.UserDAO;
import facialRecognition.FaceDetection;

import java.awt.*;

public class ILogin extends JPanel {
    private JTextField emailField;
    private FaceDetection faceDetection;
    private UserDAO userDAO;
    private IVideoModal iVideoModal;
    private MainWindow mainWindow;

    public ILogin(MainWindow mainWindow) {
        faceDetection = new FaceDetection();
        userDAO = new UserDAO();
        iVideoModal = new IVideoModal(mainWindow, "Login Biométrico");
        this.mainWindow = mainWindow;
        // Definindo preferências de tamanho e layout
        setPreferredSize(new Dimension(400, 230));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // -------------------Título-------------------
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // -------------------Formulário-------------------
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Campo de e-mail
        JLabel emailLabel = new JLabel("Seu e-mail:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(300, 30)); // Largura máxima do campo
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão de login
        JButton loginButton = new JButton("Entrar");
        loginButton.setPreferredSize(new Dimension(300, 30));
        loginButton.setMaximumSize(new Dimension(300, 30));
        loginButton.setBackground(new Color(58, 133, 191));
        loginButton.setForeground(Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adicionando componentes ao painel do formulário
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10)); 
        formPanel.add(loginButton);

        // -------------------Rodapé-------------------
        JPanel footerPanel = new JPanel();
        JLabel registerLabel = new JLabel("Ainda não tem conta?");
        JButton registerButton = new JButton("Cadastre-se");
        registerButton.setForeground(new Color(58, 133, 191));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        footerPanel.add(registerLabel);
        footerPanel.add(registerButton);

        // -------------------Eventos-------------------
        registerButton.addActionListener(e -> this.mainWindow.tooglePanel(1));
        loginButton.addActionListener(e -> login());

        // Adicionando os painéis à janela
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Login
    private void login() {
        try {
            String email = emailField.getText();
            User user = userDAO.getUsuarioByEmail(email);
            if (user == null) throw new RuntimeException("Não foi encontrado um usuário com este email!");
            iVideoModal.open();
            if (iVideoModal.bioFacial == null) throw new RuntimeException("Biometria facial não detectada!");
            if (!faceDetection.compareFaces(faceDetection.loadImage(user.getBioFacial()), iVideoModal.bioFacial))
                throw new RuntimeException("Biometria facial reconhecida para este usuário!");
            this.mainWindow.sessionUSer = user;
            this.mainWindow.tooglePanel(2);
            this.mainWindow.currentPanel.show();
            JOptionPane.showMessageDialog(
                    null,
                    "Login realizado com sucesso!",
                    "Autenticação concluída",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ocorreu o seguinte erro ao realizar o login: "+e.getMessage(),
                    "Erro ao cadastrar!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
