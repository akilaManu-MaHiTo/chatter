package com.example.chatter.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.chatter.model.Message;
import com.example.chatter.service.WebSocketSessionManager;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager webSocketSessionManager;

    @MessageMapping("/message")
    public void handleMessage(Message message){
        System.out.println("From" + message.getUser() + "Message" + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("sent");
    }

    @MessageMapping("/connect")
    public void connectUser(String userName){
        webSocketSessionManager.addOnlineUsers(userName);
        webSocketSessionManager.broadcastOnlineUsers();
        System.out.println("connected" + userName);
    }

    @MessageMapping("/disconnect")
    public void disconnectUser(String userName){
        webSocketSessionManager.removeOnlineUsers(userName);
        webSocketSessionManager.broadcastOnlineUsers();
        System.out.println("disconected" + userName);
    }

}
