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

                ChatThread cThread = new ChatThread(socket);
                Thread chat = new Thread(cThread);
                chat.start();

                ReadThread rThread = new ReadThread(socket);
                Thread read = new Thread(rThread);
                read.start();

                WriteThread wThread = new WriteThread(socket);
                Thread write = new Thread(wThread);
                write.start();
            }

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

    private class ChatThread implements Runnable {
        private Socket socket;
        
        public ChatThread(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            PrintWriter pw;
            BufferedReader br;

            try {
                pw = new PrintWriter(socket.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                pw.println("Welcome to the chat server!");
                pw.println("All messages sent will be broadcasted to the server and any connected clients.");
                pw.println("==============================================================================");

                String serverResponse = "";
                while (serverResponse != null) {
                    serverResponse = br.readLine();
                    System.out.println(serverResponse);
                }

            } catch (IOException e) {
                System.err.println("Server error with messaging platform");
            }
        }
    }
}
