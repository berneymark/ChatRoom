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
        stopRequested = true;
    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        cs.startServer();
    }
}
