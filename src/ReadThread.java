import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReadThread implements Runnable {
    private BufferedReader reader;
    private ChatClient client;
    private Socket socket;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error getting input stream: " + e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String serverResponse = reader.readLine();
                if (!serverResponse.startsWith("[" + client.getUsername() + "]:")) {
                    System.out.println(serverResponse);
                }
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e);
                break;
            }
        }
    }
}
