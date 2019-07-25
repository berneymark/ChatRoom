import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread implements Runnable {
    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        PrintWriter pw;

        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error sending message.");
        }
    }
}
