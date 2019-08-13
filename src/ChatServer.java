import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Observable;

public class ChatServer {
    public final int HOST_PORT = 7777;
    public final int USER_PORT = 7070;
    private boolean stopRequested;

    private LinkedList<UserThread> userThreads = new LinkedList<>();

    public ChatServer() {
        stopRequested = false;
    }

    public void startServer() {
        ServerGUI gui = new ServerGUI();

        try (ServerSocket serverSocket  = new ServerSocket(HOST_PORT)) {
            System.out.println("Server started at " +
                    InetAddress.getLocalHost() +
                    " on port " + HOST_PORT);
            while (!stopRequested) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection made with " + socket.getInetAddress());

                UserThread newUser = new UserThread(this, socket);
                Thread newUserThread = new Thread(newUser);
                newUserThread.start();

                addUser(newUser);
                sendUsernames();
            }
        } catch (IOException e) {
            System.err.println("Error in the server startup:  " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Server finishing");
    }

    public void broadcast(String message, UserThread exception) {
        for (UserThread user : userThreads) {
            if (user != exception)
                user.sendMessage(message);
        }
    }

    public void addUser(UserThread user) {
        userThreads.add(user);
    }

    public void removeUser(UserThread user) {
        if (userThreads.contains(user))
            userThreads.remove(user);
    }

    public boolean hasUsers() {
        return !userThreads.isEmpty();
    }

    public LinkedList<UserThread> getUsers() {
        return userThreads;
    }

    public String[] getUsernames() {
        String[] usernames = new String[getUsers().size()];

        for (int i = 0; i < getUsers().size(); i++) {
            usernames[i] = getUsers().get(i).getUsername();
        }

        return usernames;
    }

    public void sendUsernames() {
        try (ServerSocket userSocket = new ServerSocket(USER_PORT)) {
            Socket socket = userSocket.accept();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(getUsernames());
        } catch (IOException e) {}
    }

    public void requestStop() {
        System.out.println("Server shutting down.");
        stopRequested = true;
        System.exit(0);
    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        cs.startServer();
    }

    private class ServerGUI extends JFrame {
        private JPanel parentPanel;
        private JButton stopServer;
        private JLabel serverLabel;

        public ServerGUI() {
            buildGUI();
            additions();
            this.setVisible(true);
        }

        public void buildGUI() {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(300, 100);
            this.setTitle("Chat Server");
            this.getContentPane();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(
                    dim.width / 2 - this.getSize().width / 2,
                    dim.height / 2 - this.getSize().height / 2
            );

            parentPanel = new JPanel();
            this.add(parentPanel);
        }

        public void additions() {
            serverLabel = new JLabel("Server");
            parentPanel.add(serverLabel);

            stopServer = new JButton("Shutdown");
            stopServer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    requestStop();
                }
            });
            parentPanel.add(stopServer);
        }
    }
}
