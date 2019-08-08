import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    public static final String HOST_NAME = "172.28.28.9";
    public static final int HOST_PORT = 7777;

    private Thread inputThread;
    private Thread outputThread;

    private String username;

    public ChatClient() {

    }

    public void startClient() {
        try {
            Socket socket = new Socket(HOST_NAME, HOST_PORT);
            System.out.println("Welcome " + username +  "! Connected to server " + socket.getInetAddress());

            inputThread = new Thread(new ReadThread(socket, this));
            inputThread.start();

            outputThread = new Thread(new WriteThread(socket, this));
            outputThread.start();

        } catch (IOException e) {
            System.err.println("IOException : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient();
        chat.startClient();
    }
}
