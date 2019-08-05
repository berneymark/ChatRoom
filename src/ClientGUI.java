import javax.swing.*;

public class ClientGUI {
    public ClientGUI() {
        JFrame frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.getContentPane();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}
