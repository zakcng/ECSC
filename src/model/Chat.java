package model;

public class Chat {

    private transient String chatName, chatPassword;
    private Boolean chatLogs;

    public Chat(String chatName, String chatPassword, Boolean chatLogs) {
        this.chatName = chatName;
        this.chatPassword = chatPassword;
        this.chatLogs = chatLogs;
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


    @Override
    public String toString() {
        //Custom toString for displaying chat name
        return chatName;
    }
}
