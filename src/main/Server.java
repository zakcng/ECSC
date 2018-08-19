package main;
import com.sun.security.sasl.Provider;

import model.Chat;

import java.io.*;
import java.security.Security;
import java.util.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


public class Server {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "10000";
    private static final String DEFAULT_KEYSTORE = System.getProperty("user.dir") + "/data/myKeyStore.jks";
    private static final String DEFAULT_KEYSTORE_PASSWORD = "password";
    private static ArrayList<Connection> clientList = new ArrayList<>();

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
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                getConnections().add(new Connection(sslSocket));
                new Thread(new ServerThread(sslSocket)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static HashMap<String, Chat> getChats() {
        return chats;
    }

    protected static ArrayList<Connection> getConnections() {
        return clientList;
    }

    protected static void msgConnections(String msg) throws IOException {
        for (Connection c: getConnections()) {
            c.dataOutputStream.writeBytes(msg);
        }
        System.out.println("Gets to end of msgConnections");
    }

    public static void setChats(HashMap<String, Chat> chats) {
        Server.chats = chats;
    }

    //TODO - open new socket in connection which is used to broadcast messages to ips belonging to a particular chat.
    private static class Connection {
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private SSLSocket sslMsgSocket;

        public Connection(SSLSocket sslSocket) throws IOException {
            this.sslMsgSocket = sslSocket;
            this.dataInputStream = new DataInputStream(sslSocket.getInputStream());
            this.dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());
        }
    }
}
