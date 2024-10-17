package interfaces;

import facialRecognition.FaceScan;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.TableHeaderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static facialRecognition.ImageConverter.matToBufferedImage;

public class IRegister extends JFrame {
    private JTextField campoNome;
    private JTextField campoEmail;
    private JComboBox<Integer> campoNivel;
    private IVideoModal iVideoModal;

    public IRegister() {
        iVideoModal = new IVideoModal(this, "Cadastro Facial");
        setWindow();
        setcomponents();
    }

    // Configura a janela principal
    private void setWindow() {
        setTitle("Cadastro");
        setSize(850, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Cria e organiza os componentes da interface
    private void setcomponents() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setLayout(new BorderLayout());

        // Título
        JLabel labelTitulo = createTitle();
        JPanel formPanel = createForm();
        JPanel panelInferior = createFooterPanel();


        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        add(panel);
    }

    private JLabel createTitle() {
        JLabel labelTitulo = new JLabel("Cadastro de Usuário");
        labelTitulo.setFont(new Font("Verdana", Font.BOLD, 24));
        labelTitulo.setForeground(new Color(60, 60, 60));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBorder(new EmptyBorder(10, 0, 20, 0)); // Espaçamento inferior
        return labelTitulo;
    }

    private JPanel createForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Criando os campos do formulário
        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(20);
        JLabel labelEmail = new JLabel("Email:");
        campoEmail = new JTextField(20);
        JLabel labelNivel = new JLabel("Nível Permissão:");
        campoNivel = new JComboBox<>(new Integer[]{1, 2, 3});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(labelNome, gbc);
        gbc.gridx = 1;
        formPanel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(labelEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(labelNivel, gbc);
        gbc.gridx = 1;
        formPanel.add(campoNivel, gbc);

        return formPanel;
    }

    // Cria o painel inferior com o botão de cadastro
    private JPanel createFooterPanel() {
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(245, 245, 245));

        JButton botaoEnviar = new JButton("Cadastrar");
        botaoEnviar.setBackground(new Color(0, 123, 255));  // Cor de fundo do botão
        botaoEnviar.setForeground(Color.WHITE);
        botaoEnviar.setFocusPainted(false); // Remove borda de foco
        botaoEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoEnviar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Aumenta a área clicável
        botaoEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Muda o cursor para mão

        // Adicionar funcionalidade ao botão
        botaoEnviar.addActionListener(e -> iVideoModal.open());

        panelInferior.add(botaoEnviar);
        return panelInferior;
    }

    // Método para validar o formulário
    private void validarCadastro() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        JOptionPane.showMessageDialog(null, "Cadastrado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

}

