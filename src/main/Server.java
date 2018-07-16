package main;

import model.Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.Objects.hash;

public class Server {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 10000;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static final int JOIN = 0;
    private static final int CREATE = 1;
    private static final int OK = 0;
    private static final int ERROR = 1;
    //TODO load in chats
    private static HashMap<String, Chat> chats = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(10000);
        try {
            while (true) {
                //Establishes connection with client:
                Socket socket = listener.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                //Handles initial client request to join or create chat:
                int clientRequest = dataInputStream.readByte();
                handleRequest(clientRequest, dataInputStream, dataOutputStream);

                System.out.println("Chatname: " + chats.get("chat").getChatName());
                System.out.println("Password: " + chats.get("chat").getChatPassword());
                System.out.println("Is Log Enabled: " + chats.get("chat").getChatLogs());


            }
        } finally {
            listener.close();
        }
    }


    public static HashMap<String, String> addChat(HashMap<String, String> passwords,String chatName, String hashedPass) {
        passwords.put(chatName, hashedPass);
        return passwords;
    }



    public static boolean authenticate(String chatName, String hashedPass, HashMap<String, String> passwords) {
        return (hashedPass.equals(passwords.get(chatName)));
    }

    public static void handleRequest(int request, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        if (request == CREATE) {
            String chatName = dataInputStream.readUTF();
            String hashedPass = dataInputStream.readUTF();
            Boolean passEnabled = dataInputStream.readBoolean();
            Boolean logEnabled = dataInputStream.readBoolean();

            //TODO add regex for password to ensure security
            if (validChat(passEnabled, hashedPass, chatName) && !chatExists(chatName)) {
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

    public static boolean chatExists(String name) {
        return chats.containsKey(name);
    }

    public static boolean validChat(Boolean passEnabled, String hashedPass, String chatName) {
        //True if chatName is not empty, and password is enabled and valid, or if password is disabled
        return (!chatName.equals("")) && ((passEnabled && !hashedPass.equals("")) || (!passEnabled));
    }
}
