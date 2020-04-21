package edu.udacity.java.nano.model;

/**
 * WebSocket message model
 */
public class Message {
    // TODO: add message model.
    private String name;

    public Message(){

    }

    public Message(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
