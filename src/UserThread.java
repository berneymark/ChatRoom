import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread implements Runnable {
    private Socket socket;
    private ChatServer server;

    private ReadThread read;
    private WriteThread write;

    private Thread inputThread;
    private Thread outputThread;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        inputThread = new Thread(new ReadThread(socket, server));
        outputThread = new Thread(new WriteThread(socket, server));
    }
}
