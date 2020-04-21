package edu.udacity.java.nano.model;

public class MessageResponse {


    private MessageType type;
    public String content;

    public MessageResponse(){

    }

    public MessageResponse(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
