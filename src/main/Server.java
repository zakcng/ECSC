package main;
import com.sun.security.sasl.Provider;

import model.Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.util.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import static java.util.Objects.hash;

public class Server {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "10000";
    private static final String DEFAULT_KEYSTORE = System.getProperty("user.dir") + "/data/myKeyStore.jks";
    private static final String DEFAULT_KEYSTORE_PASSWORD = "password";

    //TODO load in chats
    private static HashMap<String, Chat> chats = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", DEFAULT_KEYSTORE);
        System.setProperty("javax.net.ssl.keyStorePassword", DEFAULT_KEYSTORE_PASSWORD);

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(Integer.parseInt(DEFAULT_PORT));

        while (!sslServerSocket.isClosed()) {
            try {
                System.out.println("Hi");
                new Thread(new ClientThread((SSLSocket) sslServerSocket.accept())).start();
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
