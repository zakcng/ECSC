package main;

import model.Chat;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;


public class ClientThread extends Thread {
    private static final int JOIN = 0;
    private static final int CREATE = 1;
    private static final int OK = 0;
    private static final int ERROR = 1;
    private SSLSocket sslSocket;

    public ClientThread(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
    }

    public void run() {

        try {
            System.out.println("Hello, World!");

            DataInputStream dataInputStream = new DataInputStream(sslSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());

            while (true) {
                //Handles initial client request to join or create chat:
                int clientRequest = dataInputStream.readByte();
                handleRequest(clientRequest, dataInputStream, dataOutputStream, Server.getChats());

                printChats(Server.getChats());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String, String> addChat(HashMap<String, String> passwords, String chatName, String hashedPass) {
        passwords.put(chatName, hashedPass);
        return passwords;
    }



    public static boolean authenticate(String chatName, String hashedPass, HashMap<String, String> passwords) {
        return (hashedPass.equals(passwords.get(chatName)));
    }

    public static void handleRequest(int request, DataInputStream dataInputStream, DataOutputStream dataOutputStream, HashMap<String, Chat> chats) throws IOException {
        if (request == CREATE) {
            String chatName = dataInputStream.readUTF();
            String hashedPass = dataInputStream.readUTF();
            Boolean passEnabled = dataInputStream.readBoolean();
            Boolean logEnabled = dataInputStream.readBoolean();

            //TODO add regex for password to ensure security
            if (validChat(passEnabled, hashedPass, chatName) && !chatExists(chatName, chats)) {
                hashedPass = passEnabled ? hashedPass : null;
                Chat chat = new Chat(chatName, hashedPass, logEnabled);
                chats.put(chatName, chat);
                dataOutputStream.writeByte(OK);
            } else {
                dataOutputStream.writeByte(ERROR);
            }

        } else if (request == JOIN) {
            //JOIN code goes here
        } else {
            System.out.println("Invalid request");
        }
    }

    public static boolean chatExists(String name, HashMap<String, Chat> chats) {
        return chats.containsKey(name);
    }

    public static boolean validChat(Boolean passEnabled, String hashedPass, String chatName) {
        //True if chatName is not empty, and password is enabled and valid, or if password is disabled
        return (!chatName.equals("")) && ((passEnabled && !hashedPass.equals("")) || (!passEnabled));
    }

    public static void printChats(HashMap<String, Chat> chats) {
        Iterator it = chats.entrySet().iterator();

        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

}