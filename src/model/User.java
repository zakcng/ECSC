package model;

import javax.net.ssl.SSLSocket;
import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
    private String nickname;
    private String ipAddress;
    private int passAttempts;
    private Boolean blocked;
    private SSLSocket requestSocket;

    public User(String nickname) throws Exception {
        this.nickname = nickname;
        this.passAttempts = 0;
        this.blocked = false;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setPassAttempts(int passAttempts) {
        this.passAttempts = passAttempts;
    }

    public int getPassAttempts() {
        return passAttempts;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean blocked() {
        return blocked;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public SSLSocket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(SSLSocket requestSocket) {
        this.requestSocket = requestSocket;
    }
}
