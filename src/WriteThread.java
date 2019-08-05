import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread implements Runnable {
    private PrintWriter writer;
    private Scanner scanner;
    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
        scanner = new Scanner(System.in);

        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error sending message");
        }
    }

    @Override
    public void run() {
        String scannedInput;
        do {
            scannedInput = scanner.nextLine();
            writer.println(scannedInput);
        } while (!scannedInput.equals("QUIT"));

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket");
        }
    }
}
