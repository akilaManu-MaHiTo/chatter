package com.example.chatter.client;

import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;

public class Chatter3 {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
      new Runnable() {
        @Override
        public void run() {
          ClientGui clientGui = null;
          try {
            clientGui = new ClientGui("Supun");
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
