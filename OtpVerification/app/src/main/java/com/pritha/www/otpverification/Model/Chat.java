package com.pritha.www.otpverification.Model;

public class Chat {
    private String sender;
    private String recevier;
    private String message;
    private String timestamp;

    public Chat() {
    }

    public Chat(String sender, String recevier, String message, String timestamp) {
        this.sender = sender;
        this.recevier = recevier;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecevier() {
        return recevier;
    }

    public void setRecevier(String recevier) {
        this.recevier = recevier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}


