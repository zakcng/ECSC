package main;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import model.Protocol;

import com.sun.security.sasl.Provider;
import model.User;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;




public class Client {
    //Default values provided if no arguments are provided during execution.
    private static final Integer DEFAULT_REQUEST_PORT = 10000;
    private static final Integer DEFAULT_MSG_PORT = 10001;
    private SSLSocket sslRequestSocket;
    private SSLSocket sslMsgSocket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static ObjectOutputStream objectOutputStream;
    private static final String DEFAULT_TRUSTSTORE = System.getProperty("user.dir") + "/data/myTrustStore.jts";
    private static final String DEFAULT_TRUSTORE_PASSWORD = "password";
    private ArrayList<String> chatNames = new ArrayList<>();

    /**
     * Will try to connect to default ip address and port nuber
     */
    public Client(String ip, String keyStore, String keyStorePass) throws IOException {
        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.trustStore", System.getProperty("user.dir") + "/data/" + keyStore);
        System.setProperty("javax.net.ssl.trustStorePassword", keyStorePass);

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.sslRequestSocket = (SSLSocket) sslSocketFactory.createSocket(ip, DEFAULT_REQUEST_PORT);
        this.sslMsgSocket = (SSLSocket) sslSocketFactory.createSocket(ip, DEFAULT_MSG_PORT);

        dataInputStream = new DataInputStream(sslRequestSocket.getInputStream());
        dataOutputStream = new DataOutputStream(sslRequestSocket.getOutputStream());
        objectOutputStream = new ObjectOutputStream(sslRequestSocket.getOutputStream());
    }

    /**
     * Sends request to server to create chat and receives 0 if successful or 1 if unsuccessful
     *
     * @return request result status
     * @throws IOException
     */
    public int createChat(String name, String passHash, Boolean passEnabled, Boolean logEnabled, String salt) throws IOException {
        dataOutputStream.writeByte(Protocol.CREATE.ordinal());
        dataOutputStream.writeUTF(name);
        dataOutputStream.writeUTF(passHash);
        dataOutputStream.writeBoolean(passEnabled);
        dataOutputStream.writeBoolean(logEnabled);
        dataOutputStream.writeUTF(salt);
        return dataInputStream.readByte();
    }

    public static ArrayList<String> loadChats(DataInputStream in) {
        try {
            int numChats = in.readInt();
            ArrayList<String> names = new ArrayList<>();

            for (int i = 0; i < numChats; i++) {
                names.add(in.readUTF());
            }

            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Sends request to server to join chat and receives 0 if successful or 1 if unsuccessful
     *
     * @return request result status
     * @throws IOException
     */
    public int joinChat(String chatName, String pass, User user) throws IOException {
        dataOutputStream.writeByte(Protocol.JOIN.ordinal());
        dataOutputStream.writeUTF(chatName);
        String salt = dataInputStream.readUTF();
        System.out.println(salt);
        dataOutputStream.writeUTF(hash(pass + salt)); //TODO replace password with hashedPass;
        objectOutputStream.writeObject(user);
        return dataInputStream.readByte();
    }

    public int refreshChats() throws IOException {
        dataOutputStream.writeByte(Protocol.REFRESH.ordinal());
        return dataInputStream.readByte();
    }

    public int sendMsg(String msg) throws IOException {
        dataOutputStream.writeByte(Protocol.SEND.ordinal());
        dataOutputStream.writeUTF(msg);
        return dataInputStream.readByte();
    }

    public String hash(String stringToHash) {
        String temp = stringToHash;

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(temp.getBytes());

        return (Hex.toHexString(digest));
    }

    public String salt() {
        int seedBytes = 20;
        SecureRandom rng = new SecureRandom();
        byte[] salt = rng.generateSeed(seedBytes);
        return salt.toString();
    }



    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public SSLSocket getSslMsgSocket() {
        return sslMsgSocket;
    }

    public ArrayList<String> getChatNames() {
        return chatNames;
    }

    public void setChatNames(ArrayList<String> chatNames) {
        this.chatNames = chatNames;
    }
}
