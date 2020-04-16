package com.example.worker.Chat;

public class chatObject {

    private String message;
    private Boolean currentUserBool;

    public chatObject(String message, Boolean currentUserBool){
        this.message = message;
        this.currentUserBool = currentUserBool;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getCurrentUserBool() {
        return currentUserBool;
    }

    public void setCurrentUserBool(Boolean currentUserBool) {
        this.currentUserBool = currentUserBool;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
