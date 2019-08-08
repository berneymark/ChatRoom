import java.io.*;
import java.net.Socket;

public class UserThread implements Runnable {
    private ChatServer server;
    private Socket socket;

    private PrintWriter writer;
    private Thread inputThread;
    private Thread outputThread;

    private String username;

    public UserThread(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getUsername() {
        return username;
    }

    public void getUsers() {
        if (server.hasUsers()) {
            System.out.println("Connected users: " + server.getUsernames());
        } else System.out.println("No other users connected");
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            String username = reader.readLine();
            server.addUser(username);

            String serverMessage;
            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + username + "]: " + clientMessage;
                sendMessage(serverMessage);
            } while (clientMessage.equals("QUIT"));

            sendMessage(username + " has quit.");

            server.removeUser(username, this);
            socket.close();
        } catch (IOException e) {
            System.err.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
