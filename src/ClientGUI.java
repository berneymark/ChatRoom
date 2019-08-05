import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ClientGUI {
    private JButton sendMessageButton;
    private JFrame frame;
    private JLabel appTitle;
    private JPanel chatPanel;
    private JPanel chatToolbarPanel;
    private JPanel connectionPanel;
    private JPanel parentPanel;
    private JPanel sendMessageToolbarPanel;
    private JTextField sendMessageField;

    public ClientGUI() {
        GUIInit();
        setChatPanel();
        setChatToolBar();
        setSendMessageToolbar();
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

    private void setChatPanel() {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(600, 600));
        chatPanel.setBackground(Color.YELLOW);
        parentPanel.add(chatPanel, BorderLayout.WEST);
    }
    
    private void setChatToolBar() {
        chatToolbarPanel = new JPanel();
        chatToolbarPanel.setBackground(Color.RED);
        chatPanel.add(chatToolbarPanel, BorderLayout.NORTH);

        appTitle = new JLabel("Chat Room");
        chatToolbarPanel.add(appTitle);
    }

    private void setSendMessageToolbar() {
        sendMessageToolbarPanel = new JPanel();
        chatPanel.add(sendMessageToolbarPanel, BorderLayout.SOUTH);

        sendMessageField = new JTextField();
        sendMessageField.setPreferredSize(new Dimension(500, sendMessageField.getPreferredSize().height));
        sendMessageToolbarPanel.add(sendMessageField);

        sendMessageButton = new JButton("SEND");
        sendMessageToolbarPanel.add(sendMessageButton);
    }

    private void setConnectionPanel() {
        connectionPanel = new JPanel();
        connectionPanel.setPreferredSize(new Dimension(300, 600));
        connectionPanel.setBackground(Color.GREEN);
        parentPanel.add(connectionPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}
