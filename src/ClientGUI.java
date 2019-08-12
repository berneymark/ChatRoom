import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {
    private JButton sendMessageButton;
    private JFrame frame;
    private JLabel appTitle;
    private JList conversationList;
    private JPanel chatPanel;
    private JPanel chatToolbarPanel;
    private JPanel conversationPanel;
    private JPanel parentPanel;
    private JPanel sendMessageToolbarPanel;
    private JTextArea conversationText;
    private JTextField sendMessageField;

    private String clientUsername;
    private String[] connectedUsers;

    public ClientGUI() {
        GUIInit();
        setChatPanel();
        setChatToolBar();
        setConversationText();
        setSendMessageToolbar();
        setConversationPanel();
        setConversationList();
        frame.setVisible(true);

        clientUsername = JOptionPane.showInputDialog(null, "Username:");
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
        parentPanel.add(chatPanel, BorderLayout.WEST);
    }
    
    private void setChatToolBar() {
        chatToolbarPanel = new JPanel();
        chatPanel.add(chatToolbarPanel, BorderLayout.NORTH);

        appTitle = new JLabel("Chat Room");
        chatToolbarPanel.add(appTitle);
    }

    private void setConversationText() {
        conversationText = new JTextArea();
        conversationText.setEditable(false);
        chatPanel.add(conversationText, BorderLayout.CENTER);
    }

    private void setSendMessageToolbar() {
        sendMessageToolbarPanel = new JPanel();
        chatPanel.add(sendMessageToolbarPanel, BorderLayout.SOUTH);

        sendMessageField = new JTextField();
        sendMessageField.setPreferredSize(new Dimension(500, sendMessageField.getPreferredSize().height));
        sendMessageToolbarPanel.add(sendMessageField);

        sendMessageButton = new JButton("SEND");
        sendMessageButton.addActionListener(new GUIActionListeners());
        sendMessageToolbarPanel.add(sendMessageButton);
    }

    private void setConversationPanel() {
        conversationPanel = new JPanel();
        conversationPanel.setLayout(new BorderLayout());
        conversationPanel.setPreferredSize(new Dimension(300, 600));
        parentPanel.add(conversationPanel, BorderLayout.EAST);
    }

    private void setConversationList() {
        String[] testData = {"one", "two", "three", "four"};
        conversationList = new JList(testData);
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.setLayoutOrientation(JList.VERTICAL);
        conversationList.setVisibleRowCount(-1);

        conversationPanel.add(conversationList, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }

    private class GUIActionListeners implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendMessageButton) {
                if (sendMessageField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "This message is empty.");
                } else if (sendMessageField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "This message null.");
                } else {
                    conversationText.setText(conversationText.getText() + "\n[" + clientUsername + "]: " + sendMessageField.getText());
                    sendMessageField.setText("");
                }
            }
        }
    }
}
