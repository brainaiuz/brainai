package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/echo")
@Controller
public class WebSocketServletImpl {
    private static final ConcurrentHashMap<Integer, HashMap<Integer, HashSet<Session>>> peers = new ConcurrentHashMap<>(); // key: userSession, value: connections
    private static final ConcurrentHashMap<String, String> sessionIds = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(final Session session) {
        List<String> sessionParam = session.getRequestParameterMap().get("sessionId");
        if (sessionParam != null && !sessionParam.isEmpty()) {
            String[] socket = sessionParam.get(0).split("__");
            Integer company = Integer.valueOf(socket[0]);
            Integer user = Integer.valueOf(socket[1]);
            peers.computeIfAbsent(company, e -> new HashMap<>()).computeIfAbsent(user, e -> new HashSet<>()).add(session);
        }
        sessionIds.put(session.getId(), sessionParam.get(0));
        WebSocketServerObject response = new WebSocketServerObject();
        response.setEventType(WfmUiEventType.CONNECTED);
        response.setData("CONNECTED");
        sendMessage(session, response);
    }

    @OnClose
    public void onClose(final Session session) {
        String sessionId = sessionIds.get(session.getId());
        if (sessionId == null) {
            return;
        }
        Integer companyId = Integer.parseInt(sessionId.split("__")[0]);
        Integer userId = Integer.parseInt(sessionId.split("__")[1]);
        peers.get(companyId).get(userId).remove(session);
        if (peers.containsKey(companyId)) {
            HashMap<Integer, HashSet<Session>> userMap = peers.get(companyId);
            if (userMap.containsKey(userId)) {
                HashSet<Session> sessionList = userMap.get(userId);
                sessionList.remove(session);
                if (sessionList.isEmpty()) {
                    userMap.remove(userId);
                    if (userMap.isEmpty()) {
                        peers.remove(companyId);
                    }
                }
            }
        }
        sessionIds.remove(session.getId());
    }

    @OnMessage
    public void onMessage(final String message, final Session session) {
        final String id = session.getId();
        // UNCOMMENT TO START MESSAGING
//        for (final String key : peers.keySet()) {
//            for (Session peer : peers.get(key)) {
//                peer.getAsyncRemote().sendText(message);
//            }
//        }
    }

    @OnMessage
    public void onBinaryMessage(final byte[] data, final Session session) {
        final String message = new String(data, StandardCharsets.UTF_8);
        final String id = session.getId();
    }

    public static void sendMessage(WebSocketServerObject message) {
        try {
            Integer companyId = Integer.parseInt(SecurityContext.getInstance().getCompanyId());
            Integer userId = ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID();
            HashMap<Integer, HashSet<Session>> userMap = peers.get(companyId);
            if (peers.containsKey(companyId) && userMap.containsKey(userId)) {
                for (Session session : userMap.get(userId)) {
                    if (session.isOpen()) {
                        sendMessage(session, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return;
        }
    }

    public static void sendMessage(Integer userId, WebSocketServerObject message) {
        try {
            Integer companyId = Integer.parseInt(SecurityContext.getInstance().getCompanyId());
            HashMap<Integer, HashSet<Session>> userMap = peers.get(companyId);
            if (peers.containsKey(companyId) && userMap.containsKey(userId)) {
                for (Session session : userMap.get(userId)) {
                    if (session.isOpen()) {
                        sendMessage(session, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return;
        }
    }

    public static void sendMessageToAll(Integer companyId, WebSocketServerObject message) {
        try {
            HashMap<Integer, HashSet<Session>> userMap = peers.get(companyId);
            if (userMap != null && !userMap.isEmpty()) {
                userMap.values().forEach(sessions -> {
                    sessions.forEach(session -> {
                        if (session.isOpen()) {
                            sendMessage(session, message);
                        }
                    });
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return;
        }
    }

    private static void sendMessage(Session session, WebSocketServerObject message) {
        Gson gson = new Gson();
        session.getAsyncRemote().sendText(gson.toJson(message));
    }

    @OnError
    public void onError(Session session, Throwable tr) {
        System.out.println("websocket connection aborted :" + tr.getMessage());
    }

}
