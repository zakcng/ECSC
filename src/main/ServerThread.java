package main;

import model.Chat;
import model.FileManager;
import model.User;
import model.Protocol;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


public class ServerThread extends Thread {
    private SSLSocket sslSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ObjectInputStream objectInputStream;
    private static Random random = new Random();

    public ServerThread(SSLSocket sslSocket) throws IOException {
        this.sslSocket = sslSocket;
    }

    public void run() {

        try {

            DataInputStream dataInputStream = new DataInputStream(sslSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(sslSocket.getInputStream());

            while (true) {
                //Handles initial client request to join or create chat:
                int clientRequest = dataInputStream.readByte();
                handleRequest(clientRequest, dataInputStream, dataOutputStream, objectInputStream, sslSocket, Server.getChats());
            }
        } catch (EOFException e) {
            System.out.println("EOFException!!!");
            Chat chat = Server.getChatBySocket(sslSocket);
            chat.removeUser(sslSocket);
            Server.sendUpdatedUsers(chatUsersToString(chat), chat);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String, String> addChat(HashMap<String, String> passwords, String chatName, String hashedPass) {
        passwords.put(chatName, hashedPass);
        return passwords;
    }

    //Sends list of chats to client to update list
    public static void sendChats(DataOutputStream out, HashMap<String, Chat> chats) {
        Iterator it = chats.entrySet().iterator();

        try {
            out.writeInt(chats.size());

            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                out.writeUTF(chats.get(pair.getKey()).getChatName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Converts list of user nicknames to comma separated string to be sent over network.
    public static String chatUsersToString(Chat chat) {
        StringBuilder sb = new StringBuilder();
        ArrayList<User> users = chat.getUsers();

        for (int i = 0; i < users.size(); i++) {
            sb.append(users.get(i).getNickname());

            if (i < (users.size() - 1)) {
                sb.append(',');
            }
        }

        return sb.toString();
    }

    public static void handleRequest(int request, DataInputStream dataInputStream, DataOutputStream dataOutputStream,
                                     ObjectInputStream objectInputStream, SSLSocket sslSocket, HashMap<String, Chat> chats) throws IOException {
        if (request == Protocol.CREATE.ordinal()) {
            String chatName = dataInputStream.readUTF();
            String hashedPass = dataInputStream.readUTF();
            Boolean passEnabled = dataInputStream.readBoolean();
            Boolean logEnabled = dataInputStream.readBoolean();
            String salt = dataInputStream.readUTF();
            System.out.println("HANDLE REQUEST SALT:" + salt);

            //TODO add regex for password to ensure security
            if (validChat(passEnabled, hashedPass, chatName) && !chatExists(chatName, chats)) {
                hashedPass = passEnabled ? hashedPass : null;
                Chat chat = new Chat(chatName, hashedPass, logEnabled, passEnabled, salt);
                chats.put(chatName, chat);
                FileManager.encryptAndWrite("This is a secret", FileManager.chatsToString(chats), FileManager.getChatFile());
                dataOutputStream.writeByte(Protocol.OK.ordinal());
            } else {
                dataOutputStream.writeByte(Protocol.ERROR.ordinal());
            }

        } else if (request == Protocol.JOIN.ordinal()) {
            try {
                String chatName = dataInputStream.readUTF();

                Chat chat = Server.getChats().get(chatName);
                System.out.println("Handle Request JOIN salt: " + chat.getChatSalt());
                dataOutputStream.writeUTF(chat.getChatSalt());
                String hashedPass = dataInputStream.readUTF();

                User user = (User) objectInputStream.readObject();
                user.setIpAddress(sslSocket.getInetAddress().toString());
                user.setRequestSocket(sslSocket);


                System.out.println("Hashed Pass: " + hashedPass + ", chat.getChatPassword(): " + chat.getChatPassword());
                boolean passEnabled = chat.getPassEnabled();
                System.out.println(passEnabled);

                if (!passEnabled || (hashedPass.equals(chat.getChatPassword()))) {
                    chat.getUsers().add(user);
                    dataOutputStream.writeByte(Protocol.OK.ordinal());
                    Server.sendUpdatedUsers(chatUsersToString(chat), Server.getChatBySocket(sslSocket));
                } else {
                    dataOutputStream.writeByte(Protocol.ERROR.ordinal());
                }
            } catch(ClassNotFoundException e) {
                dataOutputStream.writeByte(Protocol.ERROR.ordinal());
            }
        } else if (request == Protocol.REFRESH.ordinal()) {
            if (Server.getChats().size() == 0) {
                dataOutputStream.writeByte(Protocol.ERROR.ordinal());
            } else {
                dataOutputStream.writeByte(Protocol.OK.ordinal());
                sendChats(dataOutputStream, Server.getChats());
            }
        } else if (request == Protocol.SEND.ordinal()) {
            String msg = dataInputStream.readUTF();
            System.out.println(msg);
            if (!msg.equals("")) {
                System.out.println("SEND");
                Server.msgConnections(msg, sslSocket);
                dataOutputStream.writeByte(Protocol.OK.ordinal());

            } else {
                System.out.println("Could not print message.");
                dataOutputStream.writeByte(Protocol.ERROR.ordinal());
            }
        } else {
            System.out.println("Invalid request");
        }
    }

    public static String hash(String stringToHash) {

        String temp = stringToHash;

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(temp.getBytes());

        return (Hex.toHexString(digest));
    }

    public static boolean chatExists(String name, HashMap<String, Chat> chats) {
        return chats.containsKey(name);
    }

    public static boolean validChat(Boolean passEnabled, String hashedPass, String chatName) {
        //True if chatName is not empty, and password is enabled and valid, or if password is disabled
        return (!chatName.equals("")) && ((passEnabled && !hashedPass.equals("")) || (!passEnabled));
    }

}
