import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI extends JFrame {
    private ChatClient client;

    private JPanel chatPanel;
    private JPanel chatToolbarPanel;
    private JPanel conversationPanel;
    private JPanel parentPanel;
    private JPanel sendMessageToolbarPanel;

    private DefaultListModel<String> userListModel;
    private JButton sendMessageButton;
    private JLabel appTitle;
    private JList conversationList;
    private JTextArea conversationText;
    private JTextField sendMessageField;

    private String clientUsername;
    private PrintWriter writer;
    private String[] connectedUsers;

    public ClientGUI() {
        buildGUI();

        clientUsername = JOptionPane.showInputDialog(null, "Username:");
        client = new ChatClient(this);
        client.startClient(clientUsername);

        try {
            writer = new PrintWriter(client.getSocket().getOutputStream(), true);
        } catch (IOException e) {}

        new Thread(new UpdateUsernamesThread()).start();
    }

    private void buildGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.setTitle("Chat Room");
        this.getContentPane();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
                dim.width / 2 - this.getSize().width / 2,
                dim.height / 2 - this.getSize().height / 2
        );

        parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        this.add(parentPanel);

        setChatPanel();
        setConversationPanel();

        setVisible(true);
    }

    private void setChatPanel() {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(600, 600));
        parentPanel.add(chatPanel, BorderLayout.WEST);

        setChatToolBar();
        setConversationText();
        setSendMessageToolbar();
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
        chatPanel.add(new JScrollPane(conversationText), BorderLayout.CENTER);
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
        
        setConversationList();
    }

    private void setConversationList() {
        userListModel = new DefaultListModel<>();
        conversationList = new JList(userListModel);
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.setLayoutOrientation(JList.VERTICAL);
        conversationList.setVisibleRowCount(-1);

        conversationPanel.add(conversationList, BorderLayout.CENTER);
    }

    public void printToChat(String message) {
        conversationText.append("\r\n" + message);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }

    private class GUIActionListeners implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendMessageButton || e.getSource() == sendMessageField) {
                if (sendMessageField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "This message is empty.");
                } else if (sendMessageField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "This message null.");
                } else {
                    if (conversationText.getText().equals("") || conversationText == null) {
                        String message = sendMessageField.getText();
                        conversationText.append(
                                "[" + clientUsername + "]: "
                                        + message);
                        writer.println("[" + clientUsername + "]: " + message + "\r\n");
                        sendMessageField.setText(null);
                    } else {
                        String message = sendMessageField.getText();
                        conversationText.append(
                                "\r\n" + "[" + clientUsername + "]: "
                                        + message);
                        writer.println("[" + clientUsername + "]: " + message + "\r\n");
                        sendMessageField.selectAll();
                        sendMessageField.requestFocus();
                        sendMessageField.setText(null);
                    }
                }
            }
        }
    }

    private class UpdateUsernamesThread implements Runnable {
        @Override
        public void run() {
            String[] usernames = new String[0];

            try (Socket userSocket = new Socket(client.HOST_NAME, client.USER_PORT)) {
                while (true) {
                    try {
                        ObjectInputStream objInStream = new ObjectInputStream(userSocket.getInputStream());
                        usernames = (String[]) objInStream.readObject();
                    } catch (ClassNotFoundException e) {
                        System.err.println("Failed to retrieve usernames from the server.");
                    } finally {
                        if (connectedUsers != usernames) {
                            connectedUsers = usernames;
                            userListModel.clear();
                            for (String user : connectedUsers) {
                                userListModel.addElement(user);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to connect to the user socket.");
            }
        }
    }
}
