package edu.udacity.java.nano.controller;

import edu.udacity.java.nano.model.Message;
import edu.udacity.java.nano.model.MessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Controller
@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    private Session session;
    private static HashMap<String, String> users = new HashMap<>();
    private static final Set<WebSocketChatServer> socketChatServers = new CopyOnWriteArraySet<>();

    private static void sendMessageToAll(String msg) {
        //TODO: add send message method.
    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public MessageResponse getMessage(Message message){
        return new MessageResponse("Hello, " + message.getName());

    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        //TODO: add on open connection.
        this.session = session;
        socketChatServers.add(this);
        users.put(session.getId(), username);

        Message message = new Message();
        //message.setName(username);
        broadCast(new MessageResponse("Hello, " + message.getName()));


    }

    private void broadCast(MessageResponse messageResponse) throws IOException, EncodeException {
        socketChatServers.forEach(endpoint ->{
            synchronized (endpoint){
                try {
                    endpoint.session.getBasicRemote()
                            .sendObject(messageResponse);
                }catch (IOException | EncodeException e){
                    e.printStackTrace();

                }
            }
        });
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        //TODO: add send message.
    }


    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //TODO: add close connection.
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
