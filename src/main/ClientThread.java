package main;

import controller.Controller;
import view.ChatPane;

import java.io.IOException;
import java.io.PushbackInputStream;

public class ClientThread extends Thread {
    private Client client;
    private ChatPane chatPane;
    private PushbackInputStream pushbackInputStream;
    private static final int MESSAGE = 4;

    public ClientThread(Client client, ChatPane chatPane) throws IOException {
        this.client = client;
        this.chatPane = chatPane;
        this.pushbackInputStream = new PushbackInputStream(client.getDataInputStream());
    }

    public void run() {

        try {

            while (true) {
                if (pushbackInputStream.available() > 0) {
                    int bytesAvailable = pushbackInputStream.available();
                    pushbackInputStream.mark(bytesAvailable);
                    int id = pushbackInputStream.read();
                    System.out.println("A man can dream");
                    if (id == MESSAGE) {
                        byte message[] = new byte[bytesAvailable];
                        pushbackInputStream.read(message, 0, bytesAvailable);
                        System.out.println("Message Received: " + message.toString());
                        chatPane.appendLineToTxtMessages(message.toString());
                    } else {
                        pushbackInputStream.unread(id);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
