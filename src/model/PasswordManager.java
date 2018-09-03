package model;

import java.util.Scanner;

public class PasswordManager {
    private transient String ip;
    private transient String keyStore;
    private transient String keyStorePass;

    public PasswordManager(boolean userFlag) {
        Scanner scanner = new Scanner(System.in);

        if (userFlag) {
            System.out.print("Enter the ip of the chat server:\n");
            ip = scanner.nextLine();
        }

        System.out.print("Enter the name of your key store file:\n");
        keyStore = scanner.nextLine();

        System.out.print("Enter your key store password:\n");
        keyStorePass = scanner.nextLine();
    }

    public String getIp() {
        return ip;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public String getKeyStore() {
        return keyStore;
    }
}
