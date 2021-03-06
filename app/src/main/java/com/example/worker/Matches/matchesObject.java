package com.example.worker.Matches;

public class matchesObject {

    //uid used for name
    private String user, uid, phone, description, profilepicURL;

    public matchesObject(String user, String uid, String phone, String description, String profile){
        this.uid = uid;
        this.phone = phone;
        this.description =description;
        this.profilepicURL = profile;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfilepicURL() {
        return profilepicURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfilepicURL(String profilepicURL) {
        this.profilepicURL = profilepicURL;
    }
}
