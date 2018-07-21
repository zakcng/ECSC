package main;

import model.Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static java.util.Objects.hash;

public class Server {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 10000;

    //TODO load in chats
    private static HashMap<String, Chat> chats = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(10000);

        while (!listener.isClosed()) {
            try {
                System.out.println("Hi");
                new Thread(new ClientThread(listener.accept())).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<String, Chat> getChats() {
        return chats;
    }

    public static void setChats(HashMap<String, Chat> chats) {
        Server.chats = chats;
    }
}
