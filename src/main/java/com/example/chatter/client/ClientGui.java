package com.example.chatter.client;

import com.example.chatter.model.Message;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class ClientGui extends JFrame {

  private JPanel connectedOnlineUsersComp;
  private JPanel messageComp;

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
    getContentPane().setBackground(Utilities.PRIMARY_COLOR);
    addGuiComponent();
  }

  private void addGuiComponent() {
    addOnlineUsersComponent();
    addChatComponent();
  }

  private void addOnlineUsersComponent() {
    connectedOnlineUsersComp = new JPanel();
    connectedOnlineUsersComp.setBorder(Utilities.addPadding(10, 10, 10, 10));
    connectedOnlineUsersComp.setLayout(
      new BoxLayout(connectedOnlineUsersComp, BoxLayout.Y_AXIS)
    );
    connectedOnlineUsersComp.setBackground(Utilities.SECONDARY_COLOR);
    connectedOnlineUsersComp.setPreferredSize(new Dimension(200, getHeight()));

    JLabel connectedOnlineUsersLabel = new JLabel("Online Users");
    connectedOnlineUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
    connectedOnlineUsersLabel.setForeground(Utilities.TEXT_COLOR);
    connectedOnlineUsersComp.add(connectedOnlineUsersLabel);

    add(connectedOnlineUsersComp, BorderLayout.WEST);
  }

  private void addChatComponent() {
    JPanel chatPanel = new JPanel();
    chatPanel.setLayout(new BorderLayout());
    chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);
    chatPanel.setOpaque(false);

    messageComp = new JPanel();
    messageComp.setLayout(new BoxLayout(messageComp, BoxLayout.Y_AXIS));
    chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);
    messageComp.setOpaque(false);

    chatPanel.add(messageComp, BorderLayout.CENTER);

    // JLabel message = new JLabel("Random Chat");
    // message.setFont(new Font("Inter", Font.BOLD, 10));
    // message.setForeground(Utilities.TEXT_COLOR);
    // messageComp.add(message);

    messageComp.add(
      createChatMessageComponent(new Message("Akila", "Hi Bby!"))
    );

    JPanel inputPanel = new JPanel();
    inputPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
    inputPanel.setLayout(new BorderLayout());
    inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

    JTextField inputField = new JTextField();
    inputField.setBackground(Utilities.SECONDARY_COLOR);
    inputField.setForeground(Utilities.TEXT_COLOR);
    inputField.setFont(new Font("Inter", Font.PLAIN, 15));
    inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
    inputPanel.add(inputField, BorderLayout.CENTER);
    chatPanel.add(inputPanel, BorderLayout.SOUTH);

    add(chatPanel, BorderLayout.CENTER);
  }

  private JPanel createChatMessageComponent(Message message) {
    JPanel chatMessage = new JPanel();
    chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
    chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
    chatMessage.setBorder(Utilities.addPadding(20, 20, 10, 20));

    JLabel userNameLabel = new JLabel(message.getUser());
    userNameLabel.setFont(new Font("Inter", Font.BOLD, 18));
    userNameLabel.setForeground(Utilities.TEXT_COLOR);
    chatMessage.add(userNameLabel);

    JLabel messageLabel = new JLabel(message.getMessage());
    messageLabel.setFont(new Font("Inter", Font.PLAIN, 15));
    messageLabel.setForeground(Utilities.TEXT_COLOR);
    chatMessage.add(messageLabel);

    return chatMessage;
  }
}
