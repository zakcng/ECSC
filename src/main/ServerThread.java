package main;

import model.Chat;
import model.User;
import model.Protocol;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class ServerThread extends Thread {
    private SSLSocket sslSocket;

    public ServerThread(SSLSocket sslSocket) {
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
        } catch (Exception e) {
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

    public static void handleRequest(int request, DataInputStream dataInputStream, DataOutputStream dataOutputStream,
                                     ObjectInputStream objectInputStream, SSLSocket sslSocket, HashMap<String, Chat> chats) throws IOException {
        if (request == Protocol.CREATE.ordinal()) {
            String chatName = dataInputStream.readUTF();
            String hashedPass = dataInputStream.readUTF();
            Boolean passEnabled = dataInputStream.readBoolean();
            Boolean logEnabled = dataInputStream.readBoolean();

            //TODO add regex for password to ensure security
            if (validChat(passEnabled, hashedPass, chatName) && !chatExists(chatName, chats)) {
                hashedPass = passEnabled ? hashedPass : null;
                Chat chat = new Chat(chatName, hashedPass, logEnabled, passEnabled);
                chats.put(chatName, chat);
                dataOutputStream.writeByte(Protocol.OK.ordinal());
            } else {
                dataOutputStream.writeByte(Protocol.ERROR.ordinal());
            }

        } else if (request == Protocol.JOIN.ordinal()) {
            try {
                String chatName = dataInputStream.readUTF();
                String hashedPass = dataInputStream.readUTF();
                User user = (User) objectInputStream.readObject();
                Chat chat = Server.getChats().get(chatName);

                System.out.println("Hashed Pass: " + hashedPass + ", chat.getChatPassword(): " + chat.getChatPassword());

                if (!chat.getPassEnabled() || (hashedPass.equals(chat.getChatPassword()) && !user.blocked())) {
                    chat.getUsers().add(user);
                    dataOutputStream.writeByte(Protocol.OK.ordinal());
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
                Server.msgConnections(msg);
                dataOutputStream.writeByte(Protocol.OK.ordinal());

            } else {
                System.out.println("Could not print message.");
                dataOutputStream.writeByte(Protocol.ERROR.ordinal());
            }
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
