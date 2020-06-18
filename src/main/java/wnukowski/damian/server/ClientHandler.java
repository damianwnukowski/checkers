package wnukowski.damian.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private static final Charset COMMUNICATION_CHARSET = StandardCharsets.UTF_8;

    private Socket socket;
    private boolean shouldBeRunning = false;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), COMMUNICATION_CHARSET));
            PrintWriter printWriter = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), COMMUNICATION_CHARSET),
                    true);
            while (shouldBeRunning) {
                String command = bufferedReader.readLine();
                String response; //= processCommand
            }
        } catch (IOException ioException) {
            log.error("An error occurred obtaini");
        }
    }
}
