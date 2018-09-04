package model;

import java.util.Scanner;

public class PasswordManager {
    private transient String ip;
    private transient String store;
    private transient String storePass;

    public PasswordManager(boolean userFlag) {
        Scanner scanner = new Scanner(System.in);

        if (userFlag) {
            System.out.print("Enter the ip of the chat server:\n");
            ip = scanner.nextLine();

            System.out.print("Enter the name of your trust store file:\n");
            store = scanner.nextLine();

            System.out.print("Enter your trust store password:\n");
            storePass = scanner.nextLine();
        } else {
            ip = null;

            System.out.print("Enter the name of your key store file:\n");
            store = scanner.nextLine();

            System.out.print("Enter your key store password:\n");
            storePass = scanner.nextLine();
        }
    }

    public String getIp() {
        return ip;
    }

    public String getStorePass() {
        return storePass;
    }

    public String getStore() {
        return store;
    }
}
