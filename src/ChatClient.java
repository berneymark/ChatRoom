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
        Scanner keyboardInput = new Scanner(System.in);

        try {
            socket = new Socket(HOST_NAME, HOST_PORT);
        } catch (IOException e) {
            System.err.println("Client could not make connection : " + e);
            System.exit(-1);
        }

        PrintWriter pw;
        BufferedReader br;

        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverResponse = br.readLine();
            System.out.println(serverResponse);

        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient();
        chat.startClient();
    }
}
