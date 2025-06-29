package com.example.chatter.client;

import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Chatter {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
      new Runnable() {
        @Override
        public void run() {
          String userName = JOptionPane.showInputDialog(
            null,
            "Enter User Name:",
            "Chattar",
            JOptionPane.QUESTION_MESSAGE
          );

          if (
            userName == null || userName.isEmpty() || userName.length() > 16
          ) {
            JOptionPane.showMessageDialog(
              null,
              "Invalid User Name",
              "Error",
              JOptionPane.ERROR_MESSAGE
            );
            return;
          }
          ClientGui clientGui = null;
          try {
            clientGui = new ClientGui(userName);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          clientGui.setVisible(true);
        }
      }
    );
  }
}
