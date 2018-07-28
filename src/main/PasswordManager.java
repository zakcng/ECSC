package main;

import model.Chat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class PasswordManager {
    private static final int OK = 0;
    private static final int ERROR = 1;
    //public native int createFile();
    //public native int addPass(String chatName, String hashedPass);
    //public native int removePass(String chatName, String hashedPass);
    //public native int validPass(String chatName, String hashedPass);
    //public native String getSalt(String chatName);


    //static {
    //    System.loadLibrary("PasswordManager");
    //}

    static void processFile(int cipherMode,String key,File inputFile,File outputFile){
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void test() {
        String key = "This is a secret";
        File inputFile = new File("data/text.txt");
        File encryptedFile = new File("data/text.encrypted");
        File decryptedFile = new File("data/decrypted-text.txt");

        try {
            processFile(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
            processFile(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
            System.out.println("Sucess");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int createFile() {
        File chats = new File("chats.txt");
        if (chats.exists()) {
            return OK;
        } else {
            return ERROR;
        }
    }

    public int addPass() {
        return 0;
    }

    public int removePass() {
        return 0;
    }

    public int validPass() {
        return 0;
    }

    public String getSalt() {
        return "Salt";
    }

}
