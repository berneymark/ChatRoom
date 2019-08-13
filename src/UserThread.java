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
            System.out.println(username + " has joined the server.");
            server.broadcast(username + " has joined the server.", this);

            server.sendUsernames();

            String clientMessage = "";
            do {
                clientMessage = reader.readLine();
                String[] message = clientMessage.split(" ");

                // COMMAND = PRIVATE MESSAGE
                if (message[1].startsWith("@")) {
                    String command = message[1];
                    clientMessage = "";
                    for (int i = 0; i < message.length; i++) {
                        if (i == message.length) {
                            clientMessage += message[i];
                        } else if (i > 1) {
                            clientMessage += message[i] + " ";
                        }
                    }
                    server.privateMessage(clientMessage, command.substring(1), this);
                // COMMAND = GET ACTIVE USERS
                } else if (message[1].startsWith("$active")) {
                    server.privateMessage(getConnectedUsers(), username, this);
                // COMMAND = DISCONNECT FROM SERVER
                } else if (message[1].startsWith("$quit")) {
                    server.removeUser(this);
                    System.out.println(username + " has disconnected.");
                    server.broadcast(username + " has disconnected.", this);
                    break;
                // NO COMMAND = BROADCAST TO ALL USERS
                } else if (message[1] != null) {
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
