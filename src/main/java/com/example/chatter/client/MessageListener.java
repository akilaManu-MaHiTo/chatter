package com.example.chatter.client;

import java.util.ArrayList;

import com.example.chatter.model.Message;

public interface MessageListener {
    void onMessageReceive(Message message);
    void onActiveUserUpdate(ArrayList<String> users);

}
