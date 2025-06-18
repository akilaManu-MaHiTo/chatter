package com.example.chatter.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSessionManager {

  private final ArrayList<String> onlineUsers = new ArrayList<>();
  private final SimpMessagingTemplate messagingTemplate;

  @Autowired
  public WebSocketSessionManager(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void addOnlineUsers(String userName) {
    onlineUsers.add(userName);
  }

  public void removeOnlineUsers(String userName) {
    onlineUsers.remove(userName);
  }

  public void broadcastOnlineUsers() {
    messagingTemplate.convertAndSend("/topic/users", onlineUsers);
    System.out.println("Broadcasting Online Users" + onlineUsers);
  }

  public boolean isUserOnline(String userName) {
    return onlineUsers.contains(userName);
  }
}
