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
        System.out.print("Enter message: ");
        message = scanner.nextLine();
        if (!message.equals("") | message != null) {
            writer.println(message);
        }
    }

    @Override
    public void run() {
        String scannedInput = "";
        do {
            sendUserMessage(scannedInput);
        } while (!scannedInput.equals("QUIT"));
    }
}
