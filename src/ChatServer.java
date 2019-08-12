import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ChatServer {
    public static final int PORT_NUMBER = 7777;
    private boolean stopRequested;

    private LinkedList<UserThread> userThreads = new LinkedList<>();

    public ChatServer() {
        stopRequested = false;
    }

    public void startServer() {
        try (ServerSocket serverSocket  = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started at " +
                    InetAddress.getLocalHost() +
                    " on port " + PORT_NUMBER);
            while (!stopRequested) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection made with " + socket.getInetAddress());

                UserThread newUser = new UserThread(this, socket);
                Thread newUserThread = new Thread(newUser);
                newUserThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error in the server:  " + e.getMessage());
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

    public void requestStop() {
        stopRequested = true;
    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        cs.startServer();
    }
}
