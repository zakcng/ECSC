package model;

import main.Server;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FileManager {
    private static File chatFile;

    //TODO - let owner of server enter key on start up
    public FileManager(String dir, String key) {
        chatFile = new File(dir);

        try {
            if (!chatFile.exists()) {
                chatFile.createNewFile();

                System.out.println("File does not exist. New file created.");
            } else {
                System.out.println("File exists");
                HashMap<String, Chat> chats = decryptAndRead(key, chatFile);
                Server.setChats(chats);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getChatFile() {
        return chatFile;
    }

    public static String chatsToString(HashMap<String, Chat> chats) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String chatName: chats.keySet()) {
            Chat chat = chats.get(chatName);
            stringBuilder.append(chat.getChatName());
            stringBuilder.append(",");
            stringBuilder.append(chat.getChatPassword());
            stringBuilder.append(",");
            stringBuilder.append(chat.getPassEnabled());
            stringBuilder.append(",");
            stringBuilder.append(chat.getChatLogs());
            stringBuilder.append(",");
            stringBuilder.append(chat.getChatSalt());
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static HashMap<String, Chat> stringToChats(String chatString) {
        Scanner scanner = new Scanner(chatString);
        HashMap<String, Chat> chats = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> chatFields = Arrays.asList(line.split(","));
            String name = chatFields.get(0);

            //Creates new chat object with fields loaded from file.
            Chat chat = new Chat(chatFields.get(0), chatFields.get(1), Boolean.parseBoolean(chatFields.get(2)),
                    Boolean.parseBoolean(chatFields.get(3)), chatFields.get(4));

            chats.put(chat.getChatName(), chat);
        }
        return chats;
    }


    public static void encryptAndWrite(String key, String input, File outputFile) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            byte[] inputBytes = new byte[(int) input.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                InvalidKeyException | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Chat> decryptAndRead(String key, File inputFile) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);
            String chatString = new String(outputBytes);
            inputStream.close();
            return stringToChats(chatString);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                InvalidKeyException | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
