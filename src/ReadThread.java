import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReadThread implements Runnable {
    private ChatClient.ReadWriteLock lock = new ChatClient.ReadWriteLock();

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
                lock.acquireLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                client.reading(true);
                String serverResponse = reader.readLine();
                if (serverResponse == null | serverResponse.equals("")) {
                    System.out.println("empty string or null");
                } else System.out.println(serverResponse);
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e);
                break;
            } finally {
                client.reading(false);
                lock.releaseLock();
            }
        }
    }
}
