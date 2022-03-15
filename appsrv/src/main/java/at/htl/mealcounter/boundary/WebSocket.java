package at.htl.mealcounter.boundary;

import at.htl.mealcounter.entity.Consumation;
import at.htl.mealcounter.entity.JsonMessageEncoder;
import at.htl.mealcounter.entity.NfcIdInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/start-websocket/{name}",encoders = JsonMessageEncoder.class)
@ApplicationScoped
public class WebSocket {


    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(javax.websocket.Session session, @javax.websocket.server.PathParam("name") String name) {
        this.sessions.put(name,session);
        System.out.println("onOpen> " + name);
    }

    @OnClose
    public void onClose(javax.websocket.Session session, @javax.websocket.server.PathParam("name") String name) {
        this.sessions.remove(name);
        System.out.println("onClose> " + name);
    }

    @OnError
    public void onError(Session session, @javax.websocket.server.PathParam("name") String name, Throwable throwable) {
        System.out.println("onError> " + name + ": " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("name") String name) {
        System.out.println("onMessage> " + name + ": " + message);
    }

    private void broadcast(List<Consumation> message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    public void broadcastConsumations(List<Consumation> consumations) {

        this.broadcast(consumations);
    }
}
