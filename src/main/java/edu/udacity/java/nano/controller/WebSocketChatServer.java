package edu.udacity.java.nano.controller;

import edu.udacity.java.nano.model.Message;
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
@ServerEndpoint(value = "/chat/{username}")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    private Session session;
    private static HashMap<String, String> users = new HashMap<>();
    private static final Set<WebSocketChatServer> socketChatServers = new CopyOnWriteArraySet<>();

    private static void sendMessageToAll(Message message) throws IOException, EncodeException {
        //TODO: add send message method.
        socketChatServers.forEach(endpoint ->{
            synchronized (endpoint){
                try {
                    endpoint.session.getBasicRemote()
                            .sendObject(message);
                }catch (IOException | EncodeException e){
                    e.printStackTrace();

                }
            }
        });
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
        message.setFrom(username);
        message.setContent("Connected!");
        sendMessageToAll(message);


    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException{
        //TODO: add send message.
        message.setFrom(users.get(session.getId()));
        sendMessageToAll(message);

    }


    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        //TODO: add close connection.
        socketChatServers.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        sendMessageToAll(message);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
