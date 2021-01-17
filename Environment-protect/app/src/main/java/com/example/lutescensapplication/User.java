package com.example.lutescensapplication;

public class User {

    private String uid;
    private String nickname;
    private String email;
    private String image;

    public User(String uid, String nickname, String email, String image) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
        this.image = image;
    }

    public User(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
