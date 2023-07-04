import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class Test extends JFrame {
   
      
          public static void main(String[] args) {
        // Cria o JFrame
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cria o JLayeredPane para gerenciar as camadas
        JLayeredPane layeredPane = new JLayeredPane() {
            // Sobrescreve o método paintComponent para desenhar a imagem de fundo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Carrega a imagem de fundo
                ImageIcon backgroundImage = new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\fundo.png");
                // Desenha a imagem de fundo no JLayeredPane
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        layeredPane.setPreferredSize(frame.getSize());
        frame.setContentPane(layeredPane);

        // Cria o JPanel sobreposto
        JPanel overlayPanel = new JPanel();
        overlayPanel.setBounds(100, 100, 200, 200);
        overlayPanel.setBackground(Color.WHITE);
        layeredPane.add(overlayPanel, new Integer(1));

        // Configura o JFrame como visível
        frame.pack();
        frame.setVisible(true);
    }
}
        
