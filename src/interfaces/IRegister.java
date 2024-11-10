package interfaces;

import javax.swing.*;

import dataBase.UserDAO;
import facialRecognition.FaceDetection;

import java.awt.*;

public class IRegister extends JPanel {
    private IVideoModal iVideoModal;
    private FaceDetection faceDetection;
    private UserDAO userDAO;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> permissionField;

    public IRegister(MainWindow mainWindow) {
        this.faceDetection = new FaceDetection();
        this.userDAO = new UserDAO();
        this.iVideoModal = new IVideoModal(mainWindow, "Cadastro Biométrico");
        // Definindo preferências de tamanho e layout
        setPreferredSize(new Dimension(400, 350));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // -------------------Título-------------------
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("CADASTRO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // -------------------Formulário-------------------
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Campo de nome
        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo de e-mail
        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(300, 30));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ComboBox de permissão
        JLabel permissionLabel = new JLabel("Permissão:");
        permissionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        permissionField = new JComboBox<>(new String[]{"Usuário", "Diretor", "Ministro"});
        permissionField.setMaximumSize(new Dimension(300, 30));
        permissionField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão de cadastro
        JButton registerButton = new JButton("Cadastrar");
        registerButton.setPreferredSize(new Dimension(300, 30));
        registerButton.setMaximumSize(new Dimension(300, 30));
        registerButton.setBackground(new Color(58, 133, 191));
        registerButton.setForeground(Color.WHITE);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adicionando componentes ao painel do formulário
        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(10)); 
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(10)); 
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(10)); 
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(10)); 
        formPanel.add(permissionLabel);
        formPanel.add(Box.createVerticalStrut(10)); 
        formPanel.add(permissionField);
        formPanel.add(Box.createVerticalStrut(20)); 
        formPanel.add(registerButton);

        // -------------------Rodapé-------------------
        JPanel footerPanel = new JPanel();
        JLabel loginLabel = new JLabel("Já possui uma conta?");
        JButton loginButton = new JButton("Entrar");
        loginButton.setForeground(new Color(58, 133, 191));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        footerPanel.add(loginLabel);
        footerPanel.add(loginButton);

        // Adicionando os painéis à janela
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // -------------------Eventos------------------
        registerButton.addActionListener(e -> register());
        loginButton.addActionListener(e -> mainWindow.tooglePanel(0));
    }

    // registrar
    public void register(){
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            Integer permission = permissionField.getSelectedIndex();

            // Validação dos dados do cadastro
            if (name.isEmpty()) throw new RuntimeException("Campo nome é obrigatório!");
            if (email.isEmpty()) throw new RuntimeException("Campo e-mail é obrigatório!");   
            if (userDAO.getUsuarioByEmail(email) != null) throw new RuntimeException("Já existe um usuário com este email cadastrado!");            
            
            // Coleta da biometria facial
            iVideoModal.open();
            if (iVideoModal.bioFacial == null) throw new RuntimeException("Biometria facial não detectada!");
            if (faceDetection.isFaceRegistered(iVideoModal.bioFacial, userDAO.getAllUsers()))
                throw new RuntimeException("Biometria facial já está cadastrada!");
            String pathFacialBio = faceDetection.saveImage(iVideoModal.bioFacial);
            
            // Inserção do usuário no banco de dados
            userDAO.insertUser(name, email, permission, pathFacialBio); 
            JOptionPane.showMessageDialog(
                this, 
                "Cadastro realizado com sucesso!",
                "Cadastro concluído",
                JOptionPane.INFORMATION_MESSAGE
            );
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ocorreu o seguinte erro ao cadastrar: "+e.getMessage(),
                    "Erro ao cadastrar!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
