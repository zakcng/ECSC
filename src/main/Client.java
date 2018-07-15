package main;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1362";
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    public static void main(String[] args) throws IOException {
        Socket s = new Socket(DEFAULT_IP, 9090);
        dataOutputStream = new DataOutputStream(s.getOutputStream());
        dataInputStream = new DataInputStream(s.getInputStream());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String msg = scanner.nextLine();
                dataOutputStream.writeUTF(msg);
                System.out.println("Server Response: " + dataInputStream.readUTF());

            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}
