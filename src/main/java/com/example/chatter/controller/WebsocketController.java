package com.example.chatter.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.chatter.model.Message;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/message")
    public void handleMessage(Message message){
        System.out.println("From" + message.getUser() + "Message" + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("sent");
    }

}
