package model;

import java.util.ArrayList;

public class Chat {

    private transient String chatName, chatPassword;
    private Boolean chatLogs, passEnabled;
    private ArrayList<User> users = new ArrayList<>();

    public Chat(String chatName, String chatPassword, Boolean chatLogs, Boolean passEnabled) {
        this.chatName = chatName;
        this.chatPassword = chatPassword;
        this.chatLogs = chatLogs;
        this.passEnabled = passEnabled;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setChatPassword(String chatPassword) {
        this.chatPassword = chatPassword;
    }

    public void setChatLogs(Boolean chatLogs) {
        this.chatLogs = chatLogs;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatPassword() {
        return chatPassword;
    }

    public Boolean getChatLogs() {
        return chatLogs;
    }

    public Boolean getPassEnabled() { return passEnabled; }

    public ArrayList<User> getUsers() {return users;}


    @Override
    public String toString() {
        //Custom toString for displaying chat name
        return chatName;
    }
}
