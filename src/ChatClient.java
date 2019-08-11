import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    public static final String HOST_NAME = "172.28.28.9";
    public static final int HOST_PORT = 7777;

    private Thread inputThread;
    private Thread outputThread;

    private boolean isServerFinished;
    private String username;

    public ChatClient() {
        isServerFinished = false;
    }

    public void startClient() {
        try {
            Socket socket = new Socket(HOST_NAME, HOST_PORT);
            System.out.println("Connected to the server " + socket.getRemoteSocketAddress());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );
            Scanner scanner = new Scanner(System.in);

            String usernameRequest = reader.readLine();
            System.out.println(usernameRequest);

            String selectUsername = scanner.nextLine();
            setUsername(selectUsername);
            writer.println(selectUsername);

            inputThread = new Thread(new ReadThread(this, socket));
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

    public void setServerFinished(boolean reading) {
        isServerFinished = reading;
    }

    public boolean isServerFinished() {
        return isServerFinished;
    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient();
        chat.startClient();
    }
}
