package com.selvaraj.buyerapp.model;

public class UserMessage {
    private String message;
    private boolean isUser;
    private String messageUser;
    private long messageTime;

    public UserMessage() {
    }

    public UserMessage(String message, boolean isUser, String messageUser, long messageTime) {
        this.message = message;
        this.isUser = isUser;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

}
