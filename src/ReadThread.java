import java.io.*;
import java.net.Socket;

public class ReadThread implements Runnable {
    private BufferedReader reader;
    private ChatClient client;

    public ReadThread(ChatClient client, BufferedReader reader) {
        this.client = client;
        this.reader = reader;
    }

    @Override
    public void run() {
        String response = "";
        while (true) {
            try {
                response = reader.readLine();
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e);
                break;
            } finally {
                if (!response.equals("")) {
                    System.out.println(response);
                    client.getGui().printToChat(response);
                }
            }
        }
    }
}
