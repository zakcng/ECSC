package main;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.Security;
import com.sun.security.sasl.Provider;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class Client {
    //Default values provided if no arguments are provided during execution.
    private final String DEFAULT_IP = "127.0.0.1";
    private static final Integer DEFAULT_PORT = 10000;
    private final int CREATE = 1;
    private final int JOIN = 0;
    private SSLSocket sslSocket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static final String DEFAULT_TRUSTSTORE = System.getProperty("user.dir") + "/data/myTrustStore.jts";
    private static final String DEFAULT_TRUSTORE_PASSWORD = "password";

    /**
     * Will try to connect to default ip address and port nuber
     */
    public Client() {
        try {
            Security.addProvider(new Provider());
            System.setProperty("javax.net.ssl.trustStore", DEFAULT_TRUSTSTORE);
            System.setProperty("javax.net.ssl.trustStorePassword", DEFAULT_TRUSTORE_PASSWORD);

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.sslSocket = (SSLSocket) sslSocketFactory.createSocket(DEFAULT_IP, DEFAULT_PORT);
            dataInputStream = new DataInputStream(sslSocket.getInputStream());
            dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
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

    public String hash(String stringToHash) {

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(stringToHash.getBytes());

        return(Hex.toHexString(digest));
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }
}
