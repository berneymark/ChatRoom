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

    @Override
    public void run() {
        String scannedInput;
        do {
            if (client.getUsername() == null) {
                System.out.print("SELECT USERNAME: ");
                scannedInput = scanner.nextLine();
                client.setUsername(scannedInput);
            } else {
                System.out.print("ENTER MESSAGE: ");
                scannedInput = scanner.nextLine();
                writer.println(scannedInput);
            }
        } while (!scannedInput.equals("QUIT"));

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket");
        }
    }
}
