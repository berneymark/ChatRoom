import javax.swing.*;
import java.awt.*;

public class ClientGUI {
    private JFrame frame;
    private JPanel connectionPanel;
    private JPanel parentPanel;

    public ClientGUI() {
        GUIInit();
        setConnectionPanel();
        frame.setVisible(true);
    }

    private void GUIInit() {
        frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.getContentPane();

        parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        frame.add(parentPanel);
    }

    private void setConnectionPanel() {
        connectionPanel = new JPanel();
        connectionPanel.setPreferredSize(new Dimension(300, 600));
        parentPanel.add(connectionPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}
