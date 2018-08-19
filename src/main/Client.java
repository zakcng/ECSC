package main;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.Security;
import java.util.ArrayList;
import model.Protocol;

import com.sun.security.sasl.Provider;
import model.User;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;




public class Client {
    //Default values provided if no arguments are provided during execution.
    private final String DEFAULT_IP = "127.0.0.1";
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
    public Client() {
        try {
            Security.addProvider(new Provider());
            System.setProperty("javax.net.ssl.trustStore", DEFAULT_TRUSTSTORE);
            System.setProperty("javax.net.ssl.trustStorePassword", DEFAULT_TRUSTORE_PASSWORD);

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.sslRequestSocket = (SSLSocket) sslSocketFactory.createSocket(DEFAULT_IP, DEFAULT_REQUEST_PORT);
            this.sslMsgSocket = (SSLSocket) sslSocketFactory.createSocket(DEFAULT_IP, DEFAULT_MSG_PORT);

            dataInputStream = new DataInputStream(sslRequestSocket.getInputStream());
            dataOutputStream = new DataOutputStream(sslRequestSocket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(sslRequestSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sends request to server to create chat and receives 0 if successful or 1 if unsuccessful
     *
     * @return request result status
     * @throws IOException
     */
    public int createChat(String name, String passHash, Boolean passEnabled, Boolean logEnabled) throws IOException {
        dataOutputStream.writeByte(Protocol.CREATE.ordinal());
        dataOutputStream.writeUTF(name);
        dataOutputStream.writeUTF(passHash);
        dataOutputStream.writeBoolean(passEnabled);
        dataOutputStream.writeBoolean(logEnabled);
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
    public int joinChat(String chatName, String hashedPass, User user) throws IOException {
        dataOutputStream.writeByte(Protocol.JOIN.ordinal());
        dataOutputStream.writeUTF(chatName);
        dataOutputStream.writeUTF(hashedPass); //TODO replace password with hashedPass
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

        //salt();

        String temp = stringToHash; //+ salt();

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(temp.getBytes());

        return (Hex.toHexString(digest));
    }

    public String salt() {
        //TODO SALT FUNCTION
       // System.out.println("Salt:");
       // System.out.println(salt().getBytes(StandardCharsets.UTF_8));
        //System.out.println("Salt:" + salt().getBytes(StandardCharsets.UTF_8));

        return " ";
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
