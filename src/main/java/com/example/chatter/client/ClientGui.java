package com.example.chatter.client;

import java.util.concurrent.ExecutionException;

import com.example.chatter.model.Message;

public class ClientGui {
    public static void main(String[] args) throws InterruptedException, ExecutionException{
        MyStompClient myStompClient = new MyStompClient("Akila");
        myStompClient.sendMessage((new Message("akila","Hii!")));
    }

}
