package model;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
    private String nickname;
    private String ipAddress;
    private int passAttempts;
    private Boolean blocked;

    public User(String nickname) throws Exception {
        this.nickname = nickname + "@" + InetAddress.getLocalHost().getHostName().toString();
        this.ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
        this.passAttempts = 0;
        this.blocked = false;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        System.out.println();
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

}
