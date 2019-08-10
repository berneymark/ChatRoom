import java.io.*;
import java.net.Socket;

public class ReadThread implements Runnable {
    private BufferedReader reader;
    private ChatClient client;
    private Socket socket;

    public ReadThread(ChatClient client, Socket socket) {
        this.client = client;
        this.socket = socket;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.err.println("Error getting input stream: " + e);
        }
    }

    @Override
    public void run() {
        String response = "";
        while (true) {
            try {
                response = reader.readLine();
                if (response.equals("Enter your username: ")) {
                    System.out.print(response);
                } else if (!response.equals("")) {
                    System.out.println("\n" + response);
                } else {
                    System.out.println("broken");
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e);
                break;
            }
        }
    }
}
