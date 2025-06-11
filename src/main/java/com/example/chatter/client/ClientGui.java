package com.example.chatter.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class ClientGui extends JFrame {

  public ClientGui(String userName) {
    super("User " + userName);
    setSize(1200, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
      new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          int confirm = JOptionPane.showConfirmDialog(
            ClientGui.this,
            "Are you sure you want to exit?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION
          );

          if (confirm == JOptionPane.YES_OPTION) {
            ClientGui.this.dispose();
          }
        }
      }
    );

    setVisible(true);
  }
}
