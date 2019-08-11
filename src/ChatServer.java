import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    public static final int PORT_NUMBER = 7777;
    private boolean stopRequested;

    private Set<String> usernames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer() {
        stopRequested = false;
    }

    public void startServer() {
        try (ServerSocket serverSocket  = new ServerSocket(PORT_NUMBER)){
            System.out.println("Server started at " +
                    InetAddress.getLocalHost() +
                    " on port " + PORT_NUMBER);
            while (!stopRequested) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection made with " + socket.getInetAddress());

                UserThread newUser = new UserThread(this, socket);
                userThreads.add(newUser);

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

    public void addUser(String username) {
        usernames.add(username);
    }

    public void removeUser(String username, UserThread user) {
        boolean removed = usernames.remove(username);
        if (removed) {
            userThreads.remove(user);
            System.out.println("The user " + username + " has disconnected.");
        }
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public boolean hasUsers() {
        return !usernames.isEmpty();
    }

    public void requestStop() {
        stopRequested = true;
    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        cs.startServer();
    }
}
