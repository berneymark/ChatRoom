import java.io.*;
import java.net.Socket;

public class ReadThread implements Runnable {
    private BufferedReader reader;
    private ChatClient client;
    private Socket socket;

    public ReadThread(ChatClient client, BufferedReader reader) {
        this.client = client;
        //this.socket = socket;
        this.reader = reader;

        //try {
        //    InputStream input = socket.getInputStream();
        //    reader = new BufferedReader(new InputStreamReader(input));
        //} catch (IOException e) {
        //    System.err.println("Error getting input stream: " + e);
        //}
    }

    @Override
    public void run() {
        String response = "";
        while (true) {
            try {
                response = reader.readLine();
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e);
                break;
            } finally {
                if (!response.equals("")) {
                    System.out.println(response);
                    client.getGui().printToChat(response);
                }
            }
        }
    }
}
