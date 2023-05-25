import java.awt.BorderLayout;
import javax.swing.*;

public class Test extends JFrame {
    public Test() {
        // Cria uma barra de menu
        JMenuBar menuBar = new JMenuBar();

        // Cria um item de menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        menuBar.add(fileMenu);

        // Cria um JPanel como o contêiner principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout()); // Define o BorderLayout como o layout do painel

        // Adiciona a barra de menu ao JPanel na região sul
        mainPanel.add(menuBar, BorderLayout.SOUTH);

        // Adiciona outros componentes ao JPanel nas regiões desejadas
        // ...

        // Define o JPanel como o conteúdo do JFrame
        setContentPane(mainPanel);

        // Configurações da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MenuBar Example");
        setSize(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Test::new);
    }
}