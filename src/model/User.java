package model;

public class User {
    private String nickname;

    public User(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
