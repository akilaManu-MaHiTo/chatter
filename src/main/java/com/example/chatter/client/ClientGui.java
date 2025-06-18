package com.example.chatter.client;

import com.example.chatter.model.Message;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class ClientGui extends JFrame implements MessageListener {

  private JPanel contactsPanel;
  private JPanel messageComp;
  private MyStompClient myStompClient;
  private String userName;
  private String selectedUserName;
  private JLabel contactName;
  private JLabel statusLabel;

  public ClientGui(String userName)
    throws InterruptedException, ExecutionException {
    super("Chattar");
    this.userName = userName;
    myStompClient = new MyStompClient(this, userName);

    setSize(800, 600);
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
            myStompClient.disconnectUser(userName);
            ClientGui.this.dispose();
          }
        }
      }
    );
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);

    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(Utilities.CHATTER_PRIMARY_COLOR);
    headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

    JLabel titleLabel = new JLabel("Chattar");
    JLabel titleIcon = new JLabel("💬");

    titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
    titleLabel.setForeground(Utilities.CHATTER_BACKGROUND_COLOR);
    titleIcon.setFont(new Font("Inter", Font.PLAIN, 20));
    titleIcon.setForeground(Utilities.CHATTER_BACKGROUND_COLOR);

    headerPanel.add(titleLabel, BorderLayout.WEST);
    headerPanel.add(titleIcon, BorderLayout.EAST);

    mainPanel.add(headerPanel, BorderLayout.NORTH);

    setContentPane(mainPanel);
    addGuiComponent();
  }

  private void addGuiComponent() {
    addContactsComponent();
    addChatComponent();
  }

  private void addContactsComponent() {
    contactsPanel = new JPanel();
    contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
    contactsPanel.setBackground(Utilities.GRAY_COLOR);
    contactsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    contactsPanel.setAlignmentY(Component.TOP_ALIGNMENT);

    JScrollPane scrollPane = new JScrollPane(
      contactsPanel,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    scrollPane.setBorder(null);
    scrollPane.setPreferredSize(new Dimension(250, getHeight()));

    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
    verticalScrollBar.setUI(new ModernScrollBarUI());
    verticalScrollBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
    verticalScrollBar.setUnitIncrement(16);
    verticalScrollBar.setBlockIncrement(50);

    contactsPanel.setDoubleBuffered(true);

    add(scrollPane, BorderLayout.WEST);
  }

  private void addChatComponent() {
    JPanel chatPanel = new JPanel();
    chatPanel.setLayout(new BorderLayout());
    chatPanel.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);

    // Chat header
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    headerPanel.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);

    contactName = new JLabel(selectedUserName);
    contactName.setFont(new Font("Inter", Font.BOLD, 16));
    contactName.setForeground(Color.BLACK);

    statusLabel = new JLabel("");
    statusLabel.setFont(new Font("Inter", Font.PLAIN, 12));
    statusLabel.setForeground(Color.GRAY);

    JPanel headerText = new JPanel();
    headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
    headerText.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);
    headerText.add(contactName);
    headerText.add(statusLabel);

    headerPanel.add(headerText, BorderLayout.WEST);
    chatPanel.add(headerPanel, BorderLayout.NORTH);

    messageComp = new JPanel();
    messageComp.setLayout(new BoxLayout(messageComp, BoxLayout.Y_AXIS));
    messageComp.setBackground(Color.WHITE);
    messageComp.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    JScrollPane scrollPane = new JScrollPane(messageComp);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getViewport().setBackground(Color.WHITE);
    chatPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel inputPanel = new JPanel();
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
    inputPanel.setLayout(new BorderLayout(8, 0));
    inputPanel.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);

    JTextField inputField = new JTextField();
    inputField.setFont(new Font("Inter", Font.PLAIN, 14));
    inputField.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Utilities.CHATTER_PRIMARY_COLOR),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
      )
    );

    JButton sendButton = new JButton("Send");
    sendButton.setBackground(Utilities.CHATTER_PRIMARY_COLOR);
    sendButton.setForeground(Color.WHITE);
    sendButton.setFont(new Font("Inter", Font.BOLD, 14));
    sendButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    sendButton.setFocusPainted(false);
    sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    Runnable sendMessage = () -> {
      String input = inputField.getText().trim();
      if (input.isEmpty()) return;
      if (selectedUserName == null || selectedUserName.isEmpty()) {
        JOptionPane.showMessageDialog(
          null,
          "Please Select A User To Send Message",
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
        inputField.setText("");
        return;
      }
      inputField.setText("");

      myStompClient.sendMessage(
        new Message(userName, input, systemDateAndTime())
      );
    };

    sendButton.addActionListener(e -> sendMessage.run());
    inputField.addKeyListener(
      new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
          if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            if (selectedUserName.isEmpty()) {
              JOptionPane.showMessageDialog(
                null,
                "Please Select A User To Send Message",
                "Error",
                JOptionPane.ERROR_MESSAGE
              );
            }
            sendMessage.run();
          }
        }
      }
    );

    inputPanel.add(inputField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);
    chatPanel.add(inputPanel, BorderLayout.SOUTH);

    add(chatPanel, BorderLayout.CENTER);
  }

  private String systemDateAndTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss"
    );
    return now.format(formatter);
  }

  private JPanel createMessageComponent(
    String message,
    String time,
    boolean isUser
  ) {
    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new BorderLayout());
    messagePanel.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);
    messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    JTextArea messageText = new JTextArea(message);
    messageText.setFont(new Font("Inter", Font.PLAIN, 14));
    messageText.setWrapStyleWord(true);
    messageText.setLineWrap(true);
    messageText.setEditable(false);
    messageText.setBackground(
      isUser ? Utilities.USER_CHAT_COLOR : Utilities.NOT_USER_CHAT_COLOR
    );
    messageText.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Utilities.GRAY_COLOR),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
      )
    );
    int maxWidth = 400;
    messageText.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
    messageText.setPreferredSize(
      new Dimension(maxWidth, messageText.getPreferredSize().height)
    );

    JPanel bubblePanel = new JPanel();
    bubblePanel.setLayout(new BoxLayout(bubblePanel, BoxLayout.Y_AXIS));
    bubblePanel.setBackground(Color.WHITE);
    bubblePanel.add(messageText);

    JLabel timeLabel = new JLabel(time);
    timeLabel.setFont(new Font("Inter", Font.PLAIN, 10));
    timeLabel.setForeground(Color.GRAY);
    timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

    bubblePanel.add(timeLabel);

    JPanel alignPanel = new JPanel();
    alignPanel.setLayout(
      new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT)
    );
    alignPanel.setBackground(Utilities.CHATTER_BACKGROUND_COLOR);
    alignPanel.add(bubblePanel);

    messagePanel.add(alignPanel, BorderLayout.CENTER);
    return messagePanel;
  }

  @Override
  public void onMessageReceive(Message message) {
    // TODO Auto-generated method stub
    String newMessage = message.getMessage();
    boolean isUser = message.getUser().equals(userName);
    String time = message.getTime();
    messageComp.add(createMessageComponent(newMessage, time, isUser));
    messageComp.revalidate();
    messageComp.repaint();
    System.out.println("onMessageReceive");
  }

  public void onActiveUserUpdate(ArrayList<String> users) {
    
    if (contactsPanel == null) return;

    contactsPanel.removeAll();

    for (String user : users) {
      if (user.equals(userName)) {
        JPanel contactPanel = new JPanel(new BorderLayout());
        contactPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        contactPanel.setBackground(Utilities.GRAY_COLOR);
        contactPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel nameLabel = new JLabel("Me");
        nameLabel.setFont(new Font("Inter", Font.BOLD, 14));
        nameLabel.setForeground(Color.BLACK);

        JLabel messageLabel = new JLabel(user);
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        messageLabel.setForeground(Color.DARK_GRAY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Utilities.GRAY_COLOR);
        textPanel.add(nameLabel);
        textPanel.add(messageLabel);

        contactPanel.add(textPanel, BorderLayout.CENTER);
        contactsPanel.add(contactPanel);
        break;
      }
    }

    for (String user : users) {
      if (!user.equals(userName)) {
        JPanel contactPanel = new JPanel(new BorderLayout());
        contactPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        contactPanel.setBackground(Utilities.GRAY_COLOR);
        contactPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel nameRow = new JPanel();
        nameRow.setLayout(new BoxLayout(nameRow, BoxLayout.X_AXIS));
        nameRow.setOpaque(false);

        JLabel nameLabel = new JLabel(user);
        nameLabel.setFont(new Font("Inter", Font.BOLD, 14));
        nameLabel.setForeground(Color.BLACK);

        JLabel onlineLabel = new JLabel("🟢");
        onlineLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        onlineLabel.setForeground(Color.GREEN);

        nameRow.add(nameLabel);
        nameRow.add(Box.createHorizontalGlue());
        nameRow.add(onlineLabel);

        JLabel messageLabel = new JLabel("Online");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        messageLabel.setForeground(Color.GREEN);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Utilities.GRAY_COLOR);
        textPanel.add(nameRow);

        contactPanel.add(textPanel, BorderLayout.CENTER);
        contactsPanel.add(contactPanel);
        contactPanel.addMouseListener(
          new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              selectedUserName = user;
              contactName.setText(selectedUserName);
              statusLabel.setText("Active Now");
              System.out.println("Selected user: " + selectedUserName);
              revalidate();
              repaint();
            }
          }
        );
      }
    }

    contactsPanel.revalidate();
    contactsPanel.repaint();
  }

}
