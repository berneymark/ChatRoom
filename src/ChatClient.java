import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static final String HOST_NAME = "localhost";
    public static final int HOST_PORT = 7777;

    public ChatClient() {

    }

    public void startClient() {
        Socket socket = null;
        Scanner scannedInput = new Scanner(System.in);

        try {
            socket = new Socket(HOST_NAME, HOST_PORT);
            System.out.println("Client connected to server " + socket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Client could not make connection : " + e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient();
        chat.startClient();
    }
}
