package main;

import controller.Controller;
import model.Protocol;
import view.ChatPane;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.List;

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
            String users;
            int serverDataCode = -1;
            while (true) {
                serverDataCode = dataInputStream.readByte();

                if (serverDataCode == Protocol.MSG.ordinal()) {
                    message = dataInputStream.readUTF();
                    chatPane.appendLineToTxtMessages(message + "\n");
                } else if (serverDataCode == Protocol.USERS.ordinal()) {
                    users = dataInputStream.readUTF();

                    List<String> nickNames = Arrays.asList(users.split(","));
                    List<String> items = chatPane.getListNicknames();

                    for (String nickName: nickNames) {
                        if (!items.contains(nickName)) {
                            chatPane.addUserToList(nickName);
                        }
                    }
                }

                serverDataCode = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
