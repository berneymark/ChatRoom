import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatThread implements Runnable {
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
