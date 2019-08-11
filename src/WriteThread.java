import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread implements Runnable {
    private PrintWriter writer;
    private Scanner scanner;
    private ChatClient client;
    private Socket socket;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
        scanner = new Scanner(System.in);

        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendServerMessage(String message) {
        message = scanner.nextLine();
        if (!message.equals("") | message != null) {
            writer.println(message);
        }
    }

    public void sendUserMessage(String message) {

    }

    @Override
    public void run() {
        String message = "";
        do {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            message = scanner.nextLine();
            if (!message.equals("") | message != null) {
                writer.println(message);
            }
        } while (!message.equals("QUIT"));

        System.out.println("Goodbye " + client.getUsername());
        client.shutdownClient();
    }
}
