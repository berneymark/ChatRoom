import java.net.Socket;

public class UserThread implements Runnable {
    private ChatServer server;
    private Socket socket;

    private Thread inputThread;
    private Thread outputThread;

    private String username;

    public UserThread(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        inputThread = new Thread(new ReadThread(socket, server));
        inputThread.start();

        outputThread = new Thread(new WriteThread(socket, server));
        outputThread.start();
    }
}
