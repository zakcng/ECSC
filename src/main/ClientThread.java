package main;

import controller.Controller;
import view.ChatPane;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class ClientThread extends Thread {
    private Client client;
    private ChatPane chatPane;
    private SSLSocket sslMsgSocket;
    private DataInputStream dataInputStream;

    public ClientThread(Client client, ChatPane chatPane) throws IOException {
        this.client = client;
        this.chatPane = chatPane;
        this.sslMsgSocket = client.getSslMsgSocket();
        this.dataInputStream = new DataInputStream(sslMsgSocket.getInputStream());
    }

    public void run() {
        try {

            String message;
            while (true) {
                message = dataInputStream.readUTF();
                chatPane.appendLineToTxtMessages(message + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
