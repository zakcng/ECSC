package model;

public class Chat {

    private String chatName, chatPassword;

    public Chat(String chatName, String chatPassword) {
        this.chatName = chatName;
        this.chatPassword = chatPassword;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setChatPassword(String chatPassword) {
        this.chatPassword = chatPassword;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatPassword() {
        return chatPassword;
    }
}
