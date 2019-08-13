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

    public String getUsername() {
        return username;
    }

    public String getConnectedUsers() {
        if (server.hasUsers()) {
            String connectedUsers = "Connected users: ";
            for (String user : server.getUsernames()) {
                connectedUsers += user + ", ";
            }

            return connectedUsers;
        } else return "No other users connected\n";
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

            server.sendUsernames();

            String clientMessage = "";
            do {
                clientMessage = reader.readLine();
                String[] message = clientMessage.split(" ");
                System.out.println("MESSAGE 0: " + message[0]);
                System.out.println("MESSAGE 1: " + message[1]);

                if (message[0].startsWith("@")) {
                    String command = message[1];
                    clientMessage = clientMessage.substring(clientMessage.indexOf(command));
                    System.out.println("COMMAND" + clientMessage);
                    server.broadcast("asdf", this);
                } else if (message[0] != null) {
                    server.broadcast(clientMessage + "\r\n", this);
                    System.out.println("NOT COMMAND" + clientMessage);
                }
            } while (true);
        } catch (IOException e) {
            System.err.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
