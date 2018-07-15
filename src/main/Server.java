package main;

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
    private static final String DEFAULT_PORT = "1362";
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                dataOutputStream.writeUTF("Send chat name:");
                dataOutputStream.flush();
                String chatName = dataInputStream.readUTF();

                dataOutputStream.writeUTF("Send password:");
                dataOutputStream.flush();
                String hashedPass = dataInputStream.readUTF();

                System.out.println("Chat name: " + chatName);
                System.out.println("Password: " + hashedPass);
                try {
                    while (true) {
                        dataOutputStream.writeUTF(dataInputStream.readUTF());
                    }

                } finally {
                    socket.close();
                }
            }
        }
        finally {
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
}
