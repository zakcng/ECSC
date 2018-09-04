package main;
import com.sun.security.sasl.Provider;

import model.*;

import java.io.*;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.net.InetAddress;

//TODO - when user connection closes remove user from chat.
public class Server {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_REQUEST_PORT = "10000";
    private static final String DEFAULT_MSG_PORT = "10001";
    private static ArrayList<Connection> clientList = new ArrayList<>();
    private static FileManager fileManager;
    private static HashMap<String, Chat> chats = new HashMap<>();

    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        PasswordManager passwordManager = new PasswordManager(false);

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", System.getProperty("user.dir") + "/data/" + passwordManager.getStore());
        System.setProperty("javax.net.ssl.keyStorePassword", passwordManager.getStorePass());


        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerRequestSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(Integer.parseInt(DEFAULT_REQUEST_PORT));
        SSLServerSocket sslServerMsgSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(Integer.parseInt(DEFAULT_MSG_PORT));

        fileManager = new FileManager("data/chats.encrypted");
        
        while (!sslServerRequestSocket.isClosed()) {
            try {
                SSLSocket sslRequestSocket = (SSLSocket) sslServerRequestSocket.accept();
                SSLSocket sslMsgSocket = (SSLSocket) sslServerMsgSocket.accept();

                if (!sslRequestSocket.getInetAddress().equals(sslMsgSocket.getInetAddress())) {
                    System.out.println("Connection dropped");
                    continue;
                }

                getConnections().add(new Connection(sslMsgSocket, sslRequestSocket));
                new Thread(new ServerThread(sslRequestSocket)).start();
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

    protected static FileManager getPasswordManager() {
        return fileManager;
    }

    protected static void msgConnections(String msg, SSLSocket senderSocket) throws IOException {
        Chat chat = getChatBySocket(senderSocket);

        if (chat == null) System.out.println("Could not send message. Chat does not exist.");
        for (Connection c: getConnections()) {
            if (chat.containsSocket(c.getSslRequestSocket())) {
                c.dataOutputStream.writeByte(Protocol.MSG.ordinal());
                c.dataOutputStream.writeUTF(msg);
            }
        }
        System.out.println("Gets to end of msgConnections");
    }

    protected static void sendUpdatedUsers(String users, Chat chat) {
        if (chat == null) System.out.println("Could not send users. Chat does not exist.");

        for (Connection c: getConnections()) {
            try {
                if (chat.containsSocket(c.getSslRequestSocket())) {
                    c.dataOutputStream.writeByte(Protocol.USERS.ordinal());
                    c.dataOutputStream.writeUTF(users);
                }
            } catch (IOException e) {
                System.out.println("Lmao");
                e.printStackTrace();
            }
        }
        System.out.println("Gets to end of msgConnections");
    }

    public static Chat getChatBySocket(SSLSocket sslSocket) {
        for (String name: chats.keySet()) {
            for (User user: chats.get(name).getUsers()) {
                if (user.getRequestSocket() == sslSocket) {
                    return chats.get(name);
                }
            }
        }

        return null;
    }

    public static void printChats(HashMap<String, Chat> chats) {
        Iterator it = chats.entrySet().iterator();

        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    public static void setChats(HashMap<String, Chat> chats) {
        Server.chats = chats;
    }

    //TODO - open new socket in connection which is used to broadcast messages to ips belonging to a particular chat.
    private static class Connection {
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private SSLSocket sslMsgSocket;
        private SSLSocket sslRequestSocket;
        private String sessionID;

        public Connection(SSLSocket sslMsgSocket, SSLSocket sslRequestSocket) throws IOException {
            this.sslMsgSocket = sslMsgSocket;
            this.dataInputStream = new DataInputStream(sslMsgSocket.getInputStream());
            this.dataOutputStream = new DataOutputStream(sslMsgSocket.getOutputStream());
            this.sslRequestSocket = sslRequestSocket;
        }

        public void setSessionID(String sessionID) {
            this.sessionID = sessionID;
        }

        public String getSessionID() {
            return sessionID;
        }

        public SSLSocket getSslMsgSocket() {
            return sslMsgSocket;
        }

        public SSLSocket getSslRequestSocket() {
            return sslRequestSocket;
        }
    }
}
