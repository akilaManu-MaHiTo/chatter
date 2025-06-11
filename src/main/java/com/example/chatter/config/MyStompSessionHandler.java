package com.example.chatter.config;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.example.chatter.model.Message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String userName;
    @Override
public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    System.out.println("Client Connected");

    try {
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message) {
                        Message message = (Message) payload;
                        System.out.println("Received message from: " + message.getUser());
                        System.out.println("Received: " + message);
                    } else {
                        System.out.println("Not ok");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Client Subscribed to /topic/messages");
    } catch (Exception e) {
        System.err.println("Error during subscription: " + e.getMessage());
        e.printStackTrace();
    }
}


    @Override 
    public void handleTransportError(StompSession session, Throwable exception){
        exception.printStackTrace();
    }
}
