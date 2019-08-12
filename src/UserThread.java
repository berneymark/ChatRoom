import java.io.*;
import java.net.Socket;

public class UserThread implements Runnable {
    private ChatServer server;
    private Socket socket;
    private PrintWriter writer;

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

    public String getConnectedUsers() {
        if (server.hasUsers())
            return "Connected users: " + server.getUsernames() + "\n";
        else return "No other users connected\n";
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

            username = reader.readLine();
            System.out.println( username + " has joined the server.");

            String clientMessage = "";

            server.addUser(this);

            do {
                clientMessage = reader.readLine();
                if (clientMessage != null) {
                    server.broadcast(clientMessage + "\r\n", this);
                }
            } while (true);


            //server.broadcast(username + " has quit.", this);
            //System.out.println(username + " has left the server.");

            //server.removeUser(this);
        } catch (IOException e) {
            System.err.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
