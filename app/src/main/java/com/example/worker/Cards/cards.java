package com.example.worker.Cards;

public class cards {
    private  String uid;
    private String name;
    private String profileImageURL;
    private String key;
    public cards(String uid, String name, String profileImageURL, String key){
        this.uid = uid;
        this.name = name;
        this.profileImageURL = profileImageURL;
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
