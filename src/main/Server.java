package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Server {
    //Default values provided if no arguments are provided during execution.
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1362";

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    Scanner scanner = new Scanner(System.in);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                    out.println(scanner.nextLine());
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
}
