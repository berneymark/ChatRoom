import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static final int PORT_NUMBER = 7777;
    private boolean stopRequested;

    public ChatServer() {
        stopRequested = false;
    }

    public void startServer() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Server started at " +
                    InetAddress.getLocalHost() +
                    " on port " + PORT_NUMBER);
        } catch (IOException e) {
            System.err.println("Server can't listen on port " + e);
            System.exit(-1);
        }

        try {
            while (!stopRequested) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection made with " + socket.getInetAddress());

                Chat chat = new Chat(socket);
                Thread thread = new Thread(chat);
                thread.start();
            }

            System.out.println("Connection cut with " + serverSocket.getInetAddress());
            serverSocket.close();

        } catch (IOException e) {
            System.err.println("Can't accept client connection : " + e);
        }

        System.out.println("Server finishing");
    }

    public void requestStop() {
        stopRequested = true;
    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        cs.startServer();
    }

    private class Chat implements Runnable {
        private Socket socket;
        
        public Chat(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            PrintWriter pw;
            BufferedReader br;

            try {
                pw = new PrintWriter(socket.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                pw.println("Hello World!");
            } catch (IOException e) {
                System.err.println("Server error with messaging platform");
            }
        }
    }
}
