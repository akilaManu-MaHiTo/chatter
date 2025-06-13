package com.example.chatter.config;

import com.example.chatter.client.MessageListener;
import com.example.chatter.model.Message;
import java.lang.reflect.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

  private String userName;
  private MessageListener messageListener;

  public MyStompSessionHandler(MessageListener messageListener,String userName) {
    this.userName = userName;
    this.messageListener = messageListener;
  }

  @Override
  public void afterConnected(
    StompSession session,
    StompHeaders connectedHeaders
  ) {
    System.out.println("Client Connected");
    session.send("/app/connect", userName);
    // session.send("/app/disconnect",userName);
    try {
      session.subscribe(
        "/topic/messages",
        new StompFrameHandler() {
          @Override
          public Type getPayloadType(StompHeaders headers) {
            return Message.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            try {
              if (payload instanceof Message) {               
                Message message = (Message) payload;
                messageListener.onMessageReceive(message);
                System.out.println(
                  "Received message from: " + message.getUser()
                );
                System.out.println("Received: " + message);
              } else {
                System.out.println("Not ok");
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      );
      System.out.println("Client Subscribed to /topic/messages");
    } catch (Exception e) {
      System.err.println("Error during subscription: " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void handleTransportError(StompSession session, Throwable exception) {
    exception.printStackTrace();
  }
}
