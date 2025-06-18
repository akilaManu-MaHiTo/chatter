package com.example.chatter.client;

import com.example.chatter.config.MyStompSessionHandler;
import com.example.chatter.model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class MyStompClient {

  private StompSession session;
  private String userName;

  public MyStompClient(MessageListener messageListener, String userName)
    throws InterruptedException, ExecutionException {
    this.userName = userName;

    List<Transport> transports = new ArrayList<>();
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));

    SockJsClient sockJsClient = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    StompSessionHandler sessionHandler = new MyStompSessionHandler(
      messageListener,
      userName
    );
    String url = "ws://localhost:8080/ws";

    session = stompClient.connectAsync((url), sessionHandler).get();
  }

  public void sendMessage(Message message) {
    try {
      session.send("/app/message", message);
      System.out.println(
        "Message:" + message.getMessage() + "From:" + message.getUser()
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void disconnectUser(String userName) {
    session.send("/app/disconnect", userName);
    System.out.println("User Disconnected" + userName);
  }
}
