import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static final String HOST_NAME = "172.28.28.9";
    public static final int HOST_PORT = 7777;

    private ClientGUI gui;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private Thread inputThread;
    private Thread outputThread;

    public ChatClient(ClientGUI gui) {
        this.gui = gui;

    }

    public void startClient(String username) {
        try {
            socket = new Socket(HOST_NAME, HOST_PORT);
            System.out.println("Connected to the server " + socket.getRemoteSocketAddress());

            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                writer = new PrintWriter(
                        socket.getOutputStream(),
                        true
                );
            } catch (IOException e) {
                System.err.println("Error connecting to IO streams.");
                e.printStackTrace();
            }
            writer.println(username);

            inputThread = new Thread(new ReadThread(this, reader));
            inputThread.start();

            //outputThread = new Thread(new WriteThread(this, socket));
            //outputThread.start();
        } catch (IOException e) {
            System.err.println("IOException : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void shutdownClient() {
        try {
            socket.close();
            reader.close();
        } catch (IOException e) {
            System.err.println("Failed to close.");
        } finally {
            writer.close();
            System.exit(0);
        }
    }

    public ClientGUI getGui() {
        return gui;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient(null);
        chat.startClient("bob");
    }
}
