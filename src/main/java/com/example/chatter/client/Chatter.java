package com.example.chatter.client;

import javax.swing.SwingUtilities;

public class Chatter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                ClientGui clientGui = new ClientGui("Akila");
                clientGui.setVisible(true);
            }
        });
    }
}
