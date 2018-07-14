package main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1362";

    public static void main(String[] args) throws IOException {
        Socket s = new Socket(DEFAULT_IP, 9090);
        BufferedReader input =
                new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        System.exit(0);
    }
}
