package interfaces;

import authentication.User;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JPanel;;

public class MainWindow extends JFrame {
    public User sessionUSer;
    private JPanel[] panels;
    public JPanel currentPanel;

    public MainWindow(){
        sessionUSer = null;
        panels = new JPanel[3];
        panels[0] = new ILogin(this);
        panels[1] = new IRegister(this);
        panels[2] = new IHome(this);
        // Configurações de interface
        setTitle("APS Reconhecimento Facial");
        setSize(850, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());  
    }

    // Inicia aplicação no painel de login
    public void start(){
        setVisible(true);
        add(panels[0]);
        currentPanel = panels[0];
    }

    // Alterna entre os painéis
    public void tooglePanel(int index){
        remove(currentPanel);
        currentPanel =  panels[index];
        add(panels[index]);

        revalidate();
        repaint();
    }
    
}

