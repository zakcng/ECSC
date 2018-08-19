package main;

import controller.Controller;
import view.ChatPane;

import java.io.IOException;
import java.io.PushbackInputStream;

public class ClientThread extends Thread {
    private Client client;
    private ChatPane chatPane;
    private PushbackInputStream pushbackInputStream;

    public ClientThread(Client client, ChatPane chatPane) throws IOException {
        this.client = client;
        this.chatPane = chatPane;
        this.pushbackInputStream = new PushbackInputStream(client.getDataInputStream());
    }

    public void run() {
        //TODO - add thread safety to all instances of sockets.
        try {
            while (true) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
