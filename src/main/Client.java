package main;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Client {
    //Default values provided if no arguments are provided during execution.
    private final String DEFAULT_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 10000;
    private final int CREATE = 1;
    private final int JOIN = 0;
    private String ipAddress;
    private int port;
    private Socket socket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    /**
     * Will try to connect to specified ipAddress and port,
     * if that fails will try to connect to default ipAddress and port,
     * if that fails then will print error message
     * @param ipAddress - ipAddress of server to connect to.
     * @param port - port of chat on server to connect to.
     */
    public Client(String ipAddress, int port) {
        try {
            this.ipAddress = ipAddress;
            this.port = port;
            this.socket = new Socket(ipAddress, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            try {
                ipAddress = DEFAULT_IP;
                port = DEFAULT_PORT;
                this.socket = new Socket(ipAddress, port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException E) {
                E.printStackTrace();
                System.exit(1);
            }

        }

    }

    /**
     * Sends request to server to create chat and receives 0 if successful or 1 if unsuccessful
     * @return request result status
     * @throws IOException
     */
    public int createChat(String name, String passHash, Boolean passEnabled, Boolean logEnabled) throws IOException {
        dataOutputStream.writeByte(CREATE);
        dataOutputStream.writeUTF(name);
        dataOutputStream.writeUTF(passHash);
        dataOutputStream.writeBoolean(passEnabled);
        dataOutputStream.writeBoolean(logEnabled);
        return dataInputStream.readByte();
    }

    /**
     * Sends request to server to join chat and receives 0 if successful or 1 if unsuccessful
     * @return request result status
     * @throws IOException
     */
    public int joinChat() throws IOException {
        dataOutputStream.writeByte(JOIN);
        return dataInputStream.readByte();
    }

    public String hash(String text) {
        return text;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }
}
